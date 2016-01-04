package com.sevenga.push.common.resp;

/**
 * Created by lizi on 15/9/7.
 */

public class DefaultResult extends BaseResult {
    public DefaultResult() {
    }

    public static DefaultResult fromResponse(ResponseWrapper responseWrapper) {
        DefaultResult result = null;
        if(responseWrapper.isServerResponse()) {
            result = new DefaultResult();
        }

        result.setResponseWrapper(responseWrapper);
        return result;
    }
}
