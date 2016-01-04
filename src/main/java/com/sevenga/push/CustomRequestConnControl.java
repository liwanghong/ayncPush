package com.sevenga.push;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import java.io.IOException;

/**
 * Created by lizi on 15/9/8.
 */
public class CustomRequestConnControl implements HttpRequestInterceptor {
    private final boolean shouldClose;

    public CustomRequestConnControl(boolean shouldClose) {
        this.shouldClose = shouldClose;
    }

    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        String method = request.getRequestLine().getMethod();
        if(!method.equalsIgnoreCase("CONNECT")) {
            if(!request.containsHeader("Connection")) {
                if(this.shouldClose) {
                    request.addHeader("Connection", "Close");
                } else {
                    request.addHeader("Connection", "Keep-Alive");
                }
            }

        }
    }
}