package com.sevenga.push.common.connection;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.common.resp.APIConnectionException;
import com.sevenga.push.common.resp.APIRequestException;
import com.sevenga.push.common.resp.ResponseWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeHttpClient implements IHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(NativeHttpClient.class);
    private static final String KEYWORDS_CONNECT_TIMED_OUT = "connect timed out";
    private static final String KEYWORDS_READ_TIMED_OUT = "Read timed out";
    private int _maxRetryTimes;
    private String _authCode;
    private HttpProxy _proxy;

    public NativeHttpClient(String authCode) {
        this(authCode, 3, (HttpProxy)null);
    }

    public NativeHttpClient(String authCode, int maxRetryTimes, HttpProxy proxy) {
        this._maxRetryTimes = 0;
        this._maxRetryTimes = maxRetryTimes;
        LOG.info("Created instance with _maxRetryTimes = " + this._maxRetryTimes);
        this._authCode = authCode;
        this._proxy = proxy;
        this.initSSL();
    }

    public ResponseWrapper sendGet(String url) throws APIConnectionException, APIRequestException {
        return this.doRequest(url, (String)null, RequestMethod.GET);
    }

    public ResponseWrapper sendDelete(String url) throws APIConnectionException, APIRequestException {
        return this.doRequest(url, (String)null, RequestMethod.DELETE);
    }

    public ResponseWrapper sendPost(String url, String content) throws APIConnectionException, APIRequestException {
        return this.doRequest(url, content, RequestMethod.POST);
    }

    public ResponseWrapper doRequest(String url, String content, RequestMethod method) throws APIConnectionException, APIRequestException {
        ResponseWrapper response = null;
        int retryTimes = 0;

        while(true) {
            try {
                response = this._doRequest(url, content, method);
                return response;
            } catch (SocketTimeoutException var7) {
                if("Read timed out".equals(var7.getMessage())) {
                    throw new APIConnectionException("Read timed out. \nRead response from JPush Server timed out. \nIf this is a Push action, you may not want to retry. \nIt may be due to slowly response from JPush server, or unstable connection. \nIf the problem persists, please let us know at support@jpush.cn.", var7, true);
                }

                if(retryTimes >= this._maxRetryTimes) {
                    throw new APIConnectionException("connect timed out. \nConnect to JPush Server timed out, and already retried some times. \nPlease ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.", var7, retryTimes);
                }

                LOG.debug("connect timed out - retry again - " + (retryTimes + 1));
                ++retryTimes;
            }
        }
    }

    private ResponseWrapper _doRequest(String url, String content, RequestMethod method) throws APIConnectionException, APIRequestException, SocketTimeoutException {
        LOG.debug("Send request - " + method.toString() + " " + url);
        if(null != content) {
            LOG.debug("Request Content - " + content);
        }

        HttpURLConnection conn = null;
        OutputStream out = null;
        StringBuffer sb = new StringBuffer();
        ResponseWrapper wrapper = new ResponseWrapper();

        try {
            URL e = new URL(url);
            if(null != this._proxy) {
                conn = (HttpURLConnection)e.openConnection(this._proxy.getNetProxy());
                if(this._proxy.isAuthenticationNeeded()) {
                    conn.setRequestProperty("Proxy-Authorization", this._proxy.getProxyAuthorization());
                    Authenticator.setDefault(new NativeHttpClient.SimpleProxyAuthenticator(this._proxy.getUsername(), this._proxy.getPassword()));
                }
            } else {
                conn = (HttpURLConnection)e.openConnection();
            }

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setUseCaches(false);
            conn.setRequestMethod(method.name());
            conn.setRequestProperty("User-Agent", "JPush-API-Java-Client");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Authorization", this._authCode);
            if(RequestMethod.GET == method) {
                conn.setDoOutput(false);
            } else if(RequestMethod.DELETE == method) {
                conn.setDoOutput(false);
            } else if(RequestMethod.POST == method) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                byte[] status = content.getBytes("UTF-8");
                conn.setRequestProperty("Content-Length", String.valueOf(status.length));
                out = conn.getOutputStream();
                out.write(status);
                out.flush();
            }

            int status1 = conn.getResponseCode();
            InputStream in = null;
            if(status1 == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }

            if(null != in) {
                InputStreamReader responseContent = new InputStreamReader(in, "UTF-8");
                char[] quota = new char[1024];

                int remaining;
                while((remaining = responseContent.read(quota)) > 0) {
                    sb.append(quota, 0, remaining);
                }
            }

            String responseContent1 = sb.toString();
            wrapper.responseCode = status1;
            wrapper.responseContent = responseContent1;
            String quota1 = conn.getHeaderField("X-Rate-Limit-Limit");
            String remaining1 = conn.getHeaderField("X-Rate-Limit-Remaining");
            String reset = conn.getHeaderField("X-Rate-Limit-Reset");
            wrapper.setRateLimit(quota1, remaining1, reset);
            if(status1 == 200) {
                LOG.debug("Succeed to get response - 200 OK");
                LOG.debug("Response Content - " + responseContent1);
            } else {
                if(status1 <= 200 || status1 >= 400) {
                    LOG.warn("Got error response - responseCode:" + status1 + ", responseContent:" + responseContent1);
                    switch(status1) {
                        case 400:
                            LOG.error("Your request params is invalid. Please check them according to error message.");
                            wrapper.setErrorObject();
                            break;
                        case 401:
                            LOG.error("Authentication failed! Please check authentication params according to docs.");
                            wrapper.setErrorObject();
                            break;
                        case 403:
                            LOG.error("Request is forbidden! Maybe your appkey is listed in blacklist?");
                            wrapper.setErrorObject();
                            break;
                        case 410:
                            LOG.error("Request resource is no longer in service. Please according to notice on official website.");
                            wrapper.setErrorObject();
                        case 429:
                            LOG.error("Too many requests! Please review your appkey\'s request quota.");
                            wrapper.setErrorObject();
                            break;
                        case 500:
                        case 502:
                        case 503:
                        case 504:
                            LOG.error("Seems encountered server error. Maybe JPush is in maintenance? Please retry later.");
                            break;
                        default:
                            LOG.error("Unexpected response.");
                    }

                    throw new APIRequestException(wrapper);
                }

                LOG.warn("Normal response but unexpected - responseCode:" + status1 + ", responseContent:" + responseContent1);
            }
        } catch (SocketTimeoutException var23) {
            if(var23.getMessage().contains("connect timed out")) {
                throw var23;
            }

            if(var23.getMessage().contains("Read timed out")) {
                throw new SocketTimeoutException("Read timed out");
            }

            LOG.debug("Connection IO error. \nCan not connect to JPush Server. Please ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.", var23);
            throw new APIConnectionException("Connection IO error. \nCan not connect to JPush Server. Please ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.", var23);
        } catch (IOException var24) {
            LOG.debug("Connection IO error. \nCan not connect to JPush Server. Please ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.", var24);
            throw new APIConnectionException("Connection IO error. \nCan not connect to JPush Server. Please ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.", var24);
        } finally {
            if(null != out) {
                try {
                    out.close();
                } catch (IOException var22) {
                    LOG.error("Failed to close stream.", var22);
                }
            }

            if(null != conn) {
                conn.disconnect();
            }

        }

        return wrapper;
    }

    protected void initSSL() {
        TrustManager[] tmCerts = new TrustManager[]{new NativeHttpClient.SimpleTrustManager()};

        try {
            SSLContext e = SSLContext.getInstance("SSL");
            e.init((KeyManager[])null, tmCerts, (SecureRandom)null);
            HttpsURLConnection.setDefaultSSLSocketFactory(e.getSocketFactory());
            NativeHttpClient.SimpleHostnameVerifier hostnameVerifier = new NativeHttpClient.SimpleHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        } catch (Exception var4) {
            LOG.error("Init SSL error", var4);
        }

    }

    private static class SimpleProxyAuthenticator extends Authenticator {
        private String username;
        private String password;

        public SimpleProxyAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.username, this.password.toCharArray());
        }
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

    private static class SimpleHostnameVerifier implements HostnameVerifier {
        private SimpleHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
