package com.sevenga.push.report;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.common.resp.BaseResult;
import com.sevenga.push.common.resp.ResponseWrapper;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReceivedsResult extends BaseResult {
    private static final Type RECEIVED_TYPE = (new TypeToken() {
    }).getType();
    @Expose
    public List<ReceivedsResult.Received> received_list = new ArrayList();

    public ReceivedsResult() {
    }

    static ReceivedsResult fromResponse(ResponseWrapper responseWrapper) {
        ReceivedsResult result = new ReceivedsResult();
        if(responseWrapper.isServerResponse()) {
            result.received_list = (List)_gson.fromJson(responseWrapper.responseContent, RECEIVED_TYPE);
        }

        result.setResponseWrapper(responseWrapper);
        return result;
    }

    public static class Received {
        @Expose
        public long msg_id;
        @Expose
        public int android_received;
        @Expose
        public int ios_apns_sent;

        public Received() {
        }
    }
}

