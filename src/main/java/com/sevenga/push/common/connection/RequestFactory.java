package com.sevenga.push.common.connection;

import org.apache.http.message.BasicHttpRequest;

/**
 * Created by lizi on 15/9/15.
 */
public interface RequestFactory {
    BasicHttpRequest createHttpRequest();
}
