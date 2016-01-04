package com.sevenga.push.push.model;

/**
 * Created by lizi on 15/9/8.
 */
import com.google.gson.*;
import com.sevenga.push.utils.Preconditions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Message implements PushModel {
    private static final String TITLE = "title";
    private static final String MSG_CONTENT = "msg_content";
    private static final String CONTENT_TYPE = "content_type";
    private static final String EXTRAS = "extras";
    private final String title;
    private final String msgContent;
    private final String contentType;
//    private final Map<String, String> extras;
//    private final Map<String, Number> numberExtras;
//    private final Map<String, Boolean> booleanExtras;
    private final String extras;

//    private Message(String title, String msgContent, String contentType, Map<String, String> extras, Map<String, Number> numberExtras, Map<String, Boolean> booleanExtras) {
//        this.title = title;
//        this.msgContent = msgContent;
//        this.contentType = contentType;
//        this.extras = extras;
//        this.numberExtras = numberExtras;
//        this.booleanExtras = booleanExtras;
//    }
    private Message(String title, String msgContent, String contentType, String extras) {
        this.title = title;
        this.msgContent = msgContent;
        this.contentType = contentType;
        this.extras = extras;
    }


    public static Message.Builder newBuilder() {
        return new Message.Builder();
    }

    public static Message content(String msgContent) {
        return (new Message.Builder()).setMsgContent(msgContent).build();
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if(null != this.title) {
            json.add("title", new JsonPrimitive(this.title));
        }

        if(null != this.msgContent) {
            json.add("msg_content", new JsonPrimitive(this.msgContent));
        }

        if(null != this.contentType) {
            json.add("content_type", new JsonPrimitive(this.contentType));
        }

        if (null != this.extras)
        {
            JsonParser parser = new JsonParser();
            JsonObject extraJson = (JsonObject)parser.parse(this.extras);
            json.add("extras", extraJson);
        }

//        JsonObject extrasObject = null;
//        if(null != this.extras || null != this.numberExtras || null != this.booleanExtras) {
//            extrasObject = new JsonObject();
//        }
//
//        Iterator i$;
//        String key;
//        if(null != this.extras) {
//            i$ = this.extras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                extrasObject.add(key, new JsonPrimitive((String)this.extras.get(key)));
//            }
//        }
//
//        if(null != this.numberExtras) {
//            i$ = this.numberExtras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                extrasObject.add(key, new JsonPrimitive((Number)this.numberExtras.get(key)));
//            }
//        }
//
//        if(null != this.booleanExtras) {
//            i$ = this.booleanExtras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                extrasObject.add(key, new JsonPrimitive((Boolean)this.booleanExtras.get(key)));
//            }
//        }
//
//        if(null != this.extras || null != this.numberExtras || null != this.booleanExtras) {
//            json.add("extras", extrasObject);
//        }

        return json;
    }

    public static class Builder {
        private String title;
        private String msgContent;
        private String contentType;
        private String extras;
//        private Map<String, String> extrasBuilder;
//        private Map<String, Number> numberExtrasBuilder;
//        private Map<String, Boolean> booleanExtrasBuilder;

        public Builder() {
        }

        public Message.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Message.Builder setMsgContent(String msgContent) {
            this.msgContent = msgContent;
            return this;
        }

        public Message.Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setExtras(String extras) {
            this.extras = extras;
            return this;
        }

        //        public Message.Builder addExtra(String key, String value) {
//            Preconditions.checkArgument(null != key && null != value, "Key/Value should not be null.");
//            if(null == this.extrasBuilder) {
//                this.extrasBuilder = new HashMap();
//            }
//
//            this.extrasBuilder.put(key, value);
//            return this;
//        }
//
//        public Message.Builder addExtras(Map<String, String> extras) {
//            Preconditions.checkArgument(null != extras, "extras should not be null.");
//            if(null == this.extrasBuilder) {
//                this.extrasBuilder = new HashMap();
//            }
//
//            Iterator i$ = extras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                String key = (String)i$.next();
//                this.extrasBuilder.put(key, extras.get(key));
//            }
//
//            return this;
//        }
//
//        public Message.Builder addExtra(String key, Number value) {
//            Preconditions.checkArgument(null != key && null != value, "Key/Value should not be null.");
//            if(null == this.numberExtrasBuilder) {
//                this.numberExtrasBuilder = new HashMap();
//            }
//
//            this.numberExtrasBuilder.put(key, value);
//            return this;
//        }
//
//        public Message.Builder addExtra(String key, Boolean value) {
//            Preconditions.checkArgument(null != key && null != value, "Key/Value should not be null.");
//            if(null == this.booleanExtrasBuilder) {
//                this.booleanExtrasBuilder = new HashMap();
//            }
//
//            this.booleanExtrasBuilder.put(key, value);
//            return this;
//        }

        public Message build() {
            Preconditions.checkArgument(null != this.msgContent, "msgContent should be set");
            //return new Message(this.title, this.msgContent, this.contentType, this.extrasBuilder, this.numberExtrasBuilder, this.booleanExtrasBuilder);
            return new Message(this.title, this.msgContent, this.contentType, this.extras);
        }
    }
}
