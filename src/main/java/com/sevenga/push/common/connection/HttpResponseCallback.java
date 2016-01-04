package com.sevenga.push.common.connection;

import com.sevenga.push.common.resp.ResponseWrapper;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.protocol.HttpCoreContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lizi on 15/9/9.
 */
public class HttpResponseCallback implements FutureCallback<HttpResponse> {
    private HttpHost httpHost;
    private BasicFuture<ResponseWrapper>  future;
    private BasicNIOConnPool pool;
    private int   maxRetryCount;
    private int   retryCount;
    private RequestFactory     requestFactory;
    private HttpAsyncRequester requester;
    private long   startTime;
    private static final Logger LOG = LoggerFactory.getLogger(HttpResponseCallback.class);

    public HttpResponseCallback(
            HttpHost httpHost,
            BasicFuture<ResponseWrapper> future,
            BasicNIOConnPool pool,
            int maxRetryCount,
            int retryCount,
            RequestFactory     requestFactory,
            HttpAsyncRequester requester
            ) {
        this.httpHost = httpHost;
        this.future = future;
        this.pool = pool;
        this.maxRetryCount = maxRetryCount;
        this.retryCount = retryCount;
        this.requestFactory = requestFactory;
        this.requester = requester;
        startTime = System.currentTimeMillis();
    }

    public void completed(HttpResponse response) {
        try {
            response.setEntity(new BufferedHttpEntity(response.getEntity()));
        } catch (IOException var4) {
            throw new RuntimeException("Could not convert HttpEntity content.");
        }

        int statusCode = response.getStatusLine().getStatusCode();
        ResponseWrapper wrapper = new ResponseWrapper();
        wrapper.responseCode = statusCode;
        long time = System.currentTimeMillis() - startTime;
        LOG.trace("Success with " + time + " statusCode " + statusCode);

        try {
            InputStream in = response.getEntity().getContent();
            StringBuffer sb = new StringBuffer();

            if (null != in) {
                InputStreamReader responseContent = new InputStreamReader(in, "UTF-8");
                char[] quota = new char[1024];

                int remaining;
                while ((remaining = responseContent.read(quota)) > 0) {
                    sb.append(quota, 0, remaining);
                }
            }
            wrapper.responseContent = sb.toString();
        }
        catch (Exception e)
        {
            LOG.error("Parser response failed ", e);
            future.failed(e);
            return;
        }

        String quota1 = response.getHeaders("X-Rate-Limit-Limit")[0].getValue();
        String remaining1 = response.getHeaders("X-Rate-Limit-Remaining")[0].getValue();
        String reset = response.getHeaders("X-Rate-Limit-Reset")[0].getValue();
        wrapper.setRateLimit(quota1, remaining1, reset);

        if (statusCode != 200)
        {
            wrapper.setErrorObject();
        }

        future.completed(wrapper);
    }


    public void failed(Exception e) {
        if (retryCount < maxRetryCount)
        {
            LOG.trace("Retry count " + retryCount);
            retryCount++;
            HttpCoreContext coreContext = HttpCoreContext.create();
            this.requester.execute(
                    new BasicAsyncRequestProducer(httpHost, requestFactory.createHttpRequest()),
                    new BasicAsyncResponseConsumer(),
                    this.pool,
                    coreContext,
                    this);
        }
        else {
            future.failed(e);
            LOG.error("Requester execute failed", e);
        }
    }

    public void cancelled() {
        future.cancel(true);
    }
}
