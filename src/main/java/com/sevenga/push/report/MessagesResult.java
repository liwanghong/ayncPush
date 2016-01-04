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

public class MessagesResult extends BaseResult {
    private static final Type MESSAGE_TYPE = (new TypeToken() {
    }).getType();
    @Expose
    public List<MessagesResult.Message> messages = new ArrayList();

    public MessagesResult() {
    }

    static MessagesResult fromResponse(ResponseWrapper responseWrapper) {
        MessagesResult result = new MessagesResult();
        if(responseWrapper.isServerResponse()) {
            result.messages = (List)_gson.fromJson(responseWrapper.responseContent, MESSAGE_TYPE);
        }

        result.setResponseWrapper(responseWrapper);
        return result;
    }

    public static class Ios {
        @Expose
        public int apns_sent;
        @Expose
        public int apns_target;
        @Expose
        public int click;

        public Ios() {
        }
    }

    public static class Android {
        @Expose
        public int received;
        @Expose
        public int target;
        @Expose
        public int online_push;
        @Expose
        public int click;

        public Android() {
        }
    }

    public static class Message {
        @Expose
        public long msg_id;
        @Expose
        public MessagesResult.Android android;
        @Expose
        public MessagesResult.Ios ios;

        public Message() {
        }
    }
}
