package com.sevenga.push.push.model.notification;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AndroidNotification extends PlatformNotification {
    public static final String NOTIFICATION_ANDROID = "android";
    private static final String TITLE = "title";
    private static final String BUILDER_ID = "builder_id";
    private final String title;
    private final int builderId;

//    private AndroidNotification(String alert, String title, int builderId, Map<String, String> extras, Map<String, Number> numberExtras, Map<String, Boolean> booleanExtras, Map<String, JsonObject> jsonExtras) {
//        super(alert, extras, numberExtras, booleanExtras, jsonExtras);
//        this.title = title;
//        this.builderId = builderId;
//    }

    private AndroidNotification(String alert, String title, int builderId, String extras) {
        super(alert, extras);
        this.title = title;
        this.builderId = builderId;
    }

    public static AndroidNotification.Builder newBuilder() {
        return new AndroidNotification.Builder();
    }

    public static AndroidNotification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }

    public String getPlatform() {
        return "android";
    }

    public JsonElement toJSON() {
        JsonObject json = super.toJSON().getAsJsonObject();
        if(this.builderId > 0) {
            json.add("builder_id", new JsonPrimitive(Integer.valueOf(this.builderId)));
        }

        if(null != this.title) {
            json.add("title", new JsonPrimitive(this.title));
        }

        return json;
    }

    public static class Builder extends com.sevenga.push.push.model.notification.PlatformNotification.Builder<AndroidNotification> {
        private String title;
        private int builderId;

        public Builder() {
        }

        public AndroidNotification.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public AndroidNotification.Builder setBuilderId(int builderId) {
            this.builderId = builderId;
            return this;
        }

        public AndroidNotification.Builder setAlert(String alert) {
            this.alert = alert;
            return this;
        }

//        public AndroidNotification.Builder addExtra(String key, String value) {
//            Preconditions.checkArgument(null != key, "Key should not be null.");
//            if(null == value) {
//                PlatformNotification.LOG.debug("Extra value is null, throw away it.");
//                return this;
//            } else {
//                if(null == this.extrasBuilder) {
//                    this.extrasBuilder = new HashMap();
//                }
//
//                this.extrasBuilder.put(key, value);
//                return this;
//            }
//        }
//
//        public AndroidNotification.Builder addExtras(Map<String, String> extras) {
//            if(null == extras) {
//                PlatformNotification.LOG.warn("Null extras param. Throw away it.");
//                return this;
//            } else {
//                if(null == this.extrasBuilder) {
//                    this.extrasBuilder = new HashMap();
//                }
//
//                Iterator i$ = extras.keySet().iterator();
//
//                while(i$.hasNext()) {
//                    String key = (String)i$.next();
//                    this.extrasBuilder.put(key, extras.get(key));
//                }
//
//                return this;
//            }
//        }
//
//        public AndroidNotification.Builder addExtra(String key, Number value) {
//            Preconditions.checkArgument(null != key, "Key should not be null.");
//            if(null == value) {
//                PlatformNotification.LOG.debug("Extra value is null, throw away it.");
//                return this;
//            } else {
//                if(null == this.numberExtrasBuilder) {
//                    this.numberExtrasBuilder = new HashMap();
//                }
//
//                this.numberExtrasBuilder.put(key, value);
//                return this;
//            }
//        }
//
//        public AndroidNotification.Builder addExtra(String key, Boolean value) {
//            Preconditions.checkArgument(null != key, "Key should not be null.");
//            if(null == value) {
//                PlatformNotification.LOG.debug("Extra value is null, throw away it.");
//                return this;
//            } else {
//                if(null == this.booleanExtrasBuilder) {
//                    this.booleanExtrasBuilder = new HashMap();
//                }
//
//                this.booleanExtrasBuilder.put(key, value);
//                return this;
//            }
//        }
//
//        public AndroidNotification.Builder addExtra(String key, JsonObject value) {
//            Preconditions.checkArgument(null != key, "Key should not be null.");
//            if(null == value) {
//                PlatformNotification.LOG.debug("Extra value is null, throw away it.");
//                return this;
//            } else {
//                if(null == this.jsonExtrasBuilder) {
//                    this.jsonExtrasBuilder = new HashMap();
//                }
//
//                this.jsonExtrasBuilder.put(key, value);
//                return this;
//            }
//        }

        public AndroidNotification build() {
            //return new AndroidNotification(this.alert, this.title, this.builderId, this.extrasBuilder, this.numberExtrasBuilder, this.booleanExtrasBuilder, this.jsonExtrasBuilder);
            return new AndroidNotification(this.alert, this.title, this.builderId, this.extras);
        }
    }
}

