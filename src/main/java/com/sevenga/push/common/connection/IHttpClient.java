package com.sevenga.push.common.connection;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.common.resp.APIConnectionException;
import com.sevenga.push.common.resp.APIRequestException;
import com.sevenga.push.common.resp.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface IHttpClient {
    String CHARSET = "UTF-8";
    String CONTENT_TYPE_JSON = "application/json";
    String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    String RATE_LIMIT_QUOTA = "X-Rate-Limit-Limit";
    String RATE_LIMIT_Remaining = "X-Rate-Limit-Remaining";
    String RATE_LIMIT_Reset = "X-Rate-Limit-Reset";
    String JPUSH_USER_AGENT = "JPush-API-Java-Client";
    int RESPONSE_OK = 200;
    String IO_ERROR_MESSAGE = "Connection IO error. \nCan not connect to JPush Server. Please ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.";
    String CONNECT_TIMED_OUT_MESSAGE = "connect timed out. \nConnect to JPush Server timed out, and already retried some times. \nPlease ensure your internet connection is ok. \nIf the problem persists, please let us know at support@jpush.cn.";
    String READ_TIMED_OUT_MESSAGE = "Read timed out. \nRead response from JPush Server timed out. \nIf this is a Push action, you may not want to retry. \nIt may be due to slowly response from JPush server, or unstable connection. \nIf the problem persists, please let us know at support@jpush.cn.";
    Gson _gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
    int DEFAULT_CONNECTION_TIMEOUT = 5000;
    int DEFAULT_READ_TIMEOUT = 30000;
    int DEFAULT_MAX_RETRY_TIMES = 3;

    ResponseWrapper sendGet(String var1) throws APIConnectionException, APIRequestException;

    ResponseWrapper sendDelete(String var1) throws APIConnectionException, APIRequestException;

    ResponseWrapper sendPost(String var1, String var2) throws APIConnectionException, APIRequestException;

    public static enum RequestMethod {
        GET,
        POST,
        DELETE;

        private RequestMethod() {
        }
    }
}
