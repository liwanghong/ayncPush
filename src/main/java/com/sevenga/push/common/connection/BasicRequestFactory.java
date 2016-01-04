package com.sevenga.push.common.connection;

import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpRequest;

/**
 * Created by lizi on 15/9/15.
 */
public class BasicRequestFactory implements RequestFactory {
    private String authCode;
    private String uri;
    private String content;
    private String method;

    public BasicRequestFactory(String authCode, String uri, String content, String method) {
        this.authCode = authCode;
        this.uri = uri;
        this.content = content;
        this.method = method;
    }

    public BasicHttpRequest createHttpRequest()
    {
        BasicHttpRequest request = new BasicHttpRequest(method, uri, HttpVersion.HTTP_1_1);
        request.setHeader("User-Agent", "JPush-API-Java-Client");
        request.setHeader("Accept-Charset", "UTF-8");
        request.setHeader("Charset", "UTF-8");
        request.setHeader("Authorization", this.authCode);
        return request;
    }
}
