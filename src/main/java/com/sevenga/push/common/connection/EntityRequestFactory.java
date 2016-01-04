package com.sevenga.push.common.connection;

import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.entity.NByteArrayEntity;

/**
 * Created by lizi on 15/9/15.
 */
public class EntityRequestFactory implements RequestFactory {
    private String authCode;
    private String uri;
    private String content;
    private String method;

    public EntityRequestFactory(String authCode, String uri, String content, String method) {
        this.authCode = authCode;
        this.uri = uri;
        this.content = content;
        this.method = method;
    }

    public BasicHttpRequest createHttpRequest()
    {
        try {
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest(method, uri, HttpVersion.HTTP_1_1);
            request.setHeader("User-Agent", "JPush-API-Java-Client");
            request.setHeader("Accept-Charset", "UTF-8");
            request.setHeader("Charset", "UTF-8");
            request.setHeader("Authorization", this.authCode);
            request.setHeader("Content-Type", "application/json");
            byte[] status = content.getBytes("UTF-8");
            NByteArrayEntity entity = new NByteArrayEntity(status);
            request.setEntity(entity);
            return request;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
