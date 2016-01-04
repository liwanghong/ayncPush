package com.sevenga.push.common.connection;

import com.sevenga.push.common.ServiceHelper;

/**
 * Created by lizi on 15/9/15.
 */
public class AsyncHttpClientFactory {
    public static AsyncHttpClient createAsyncHttpClient(String masterSecret, String appKey, int retryTimes)
    {
        ServiceHelper.checkBasic(appKey, masterSecret);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        AsyncHttpClient httpClient = new AsyncHttpClient(authCode, retryTimes);
        httpClient.init();
        return httpClient;
    }
}
