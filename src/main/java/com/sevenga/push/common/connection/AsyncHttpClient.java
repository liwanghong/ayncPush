package com.sevenga.push.common.connection;

import com.sevenga.push.common.resp.ResponseWrapper;
import org.apache.http.HttpHost;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.SSLNHttpClientConnectionFactory;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by lizi on 15/9/9.
 */
public class AsyncHttpClient{
    private static final Logger LOG = LoggerFactory.getLogger(AsyncHttpClient.class);
    private HttpAsyncRequester requester;
    private BasicNIOConnPool   pool;
    private String authCode;
    private int    retryCount;

    public AsyncHttpClient(String authCode)
    {
        this(authCode, 3);
    }

    public AsyncHttpClient(String authCode, int retryCount)
    {
        this.authCode = authCode;
        this.retryCount = retryCount;
    }

    private static class SimpleTrustManager implements TrustManager, X509TrustManager {
        private SimpleTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public boolean init()
    {
        try {
            HttpProcessor httpProc = HttpProcessorBuilder.create()
                    .add(new RequestContent())
                    .add(new RequestTargetHost())
                    .add(new RequestConnControl())
                    .add(new RequestUserAgent("MyAgent-HTTP/1.1"))
                    .add(new RequestExpectContinue(true))
                    .build();

            requester = new HttpAsyncRequester(httpProc, new NoConnectionReuseStrategy());

            TrustManager[] tmCerts = new TrustManager[]{new SimpleTrustManager()};
            SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
            sslcontext.init((KeyManager[])null, tmCerts, (SecureRandom)null);

            HttpAsyncRequestExecutor ph = new HttpAsyncRequestExecutor();


            final SSLNHttpClientConnectionFactory connFactory = new SSLNHttpClientConnectionFactory(
                    sslcontext, null, ConnectionConfig.DEFAULT);

            final IOEventDispatch ioEventDispatch = new DefaultHttpClientIODispatch(
                    ph,
                    connFactory);
            final ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.custom()
                    .setConnectTimeout(20000)
                    .setSoTimeout(20000)
                    .setTcpNoDelay(true)
                    .setIoThreadCount(2).build());


            NIOConnFactory<HttpHost, NHttpClientConnection> factory = new NIOConnFactory<HttpHost, NHttpClientConnection>() {
                public NHttpClientConnection create(HttpHost httpHost, IOSession ioSession) throws IOException {
                    NHttpClientConnection conn = connFactory.createConnection(ioSession);
                    return conn;
                }
            };

            pool = new BasicNIOConnPool(ioReactor, factory, 10000);
            pool.setMaxTotal(20);
            pool.setDefaultMaxPerRoute(20);
            Thread reactorThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        ioReactor.execute(ioEventDispatch);
                    } catch (InterruptedIOException var2) {
                        LOG.error("Interrupt exception", var2);
                    } catch (IOException var3) {
                        LOG.error("I/O error", var3);
                    }catch (Exception e){
                        LOG.error("Exception", e);
                    }
                    LOG.trace("I/O Reactor terminate");
                }
            }, "JPush thread");
            reactorThread.start();
            return true;
        }
        catch (IOReactorException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public BasicFuture<ResponseWrapper> sendGet(String host, String uri, FutureCallback<ResponseWrapper> callback)
    {
        return sendBasicRequest(host, uri, "GET", callback);
    }


    public BasicFuture<ResponseWrapper> sendDelete(String host, String uri, FutureCallback<ResponseWrapper> callback)
    {
        return sendBasicRequest(host, uri, "DELETE", callback);
    }

    public BasicFuture<ResponseWrapper> sendPost(String host,
                                         String uri,
                                         String content,
                                         FutureCallback<ResponseWrapper> callback)
    {
        try {
            EntityRequestFactory requestFactory = new EntityRequestFactory(authCode, uri, content, "POST");
            HttpHost httpHost = new HttpHost(host, 443, "https");
            HttpCoreContext coreContext = HttpCoreContext.create();
            BasicFuture<ResponseWrapper> future = new BasicFuture<ResponseWrapper>(callback);
            this.requester.execute(
                    new BasicAsyncRequestProducer(httpHost, requestFactory.createHttpRequest()),
                    new BasicAsyncResponseConsumer(),
                    this.pool,
                    coreContext,
                    new HttpResponseCallback(httpHost, future, pool, retryCount, 0, requestFactory, requester));
            return future;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private BasicFuture<ResponseWrapper> sendBasicRequest(String host,
                                                          String uri,
                                                          String method,
                                                          FutureCallback<ResponseWrapper> callback)
    {
        try {
            BasicRequestFactory requestFactory = new BasicRequestFactory(authCode, uri, "", method);

            HttpHost httpHost = new HttpHost(host, 443, "https");
            HttpCoreContext coreContext = HttpCoreContext.create();

            BasicFuture<ResponseWrapper> future = new BasicFuture<ResponseWrapper>(callback);

            this.requester.execute(
                    new BasicAsyncRequestProducer(httpHost, requestFactory.createHttpRequest()),
                    new BasicAsyncResponseConsumer(),
                    this.pool,
                    coreContext,
                    new HttpResponseCallback(httpHost, future, pool, retryCount, 0, requestFactory, requester));
            return future;
        }
        catch (Exception e)
        {
            LOG.error("Requester execute failed", e);
        }
        return null;
    }
}
