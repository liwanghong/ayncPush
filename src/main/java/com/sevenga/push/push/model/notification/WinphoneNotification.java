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

public class WinphoneNotification extends PlatformNotification {
    private static final String NOTIFICATION_WINPHONE = "winphone";
    private static final String TITLE = "title";
    private static final String _OPEN_PAGE = "_open_page";
    private final String title;
    private final String openPage;

//    private WinphoneNotification(String alert, String title, String openPage, Map<String, String> extras, Map<String, Number> numberExtras, Map<String, Boolean> booleanExtras, Map<String, JsonObject> jsonExtras) {
//        super(alert, extras, numberExtras, booleanExtras, jsonExtras);
//        this.title = title;
//        this.openPage = openPage;
//    }

    private WinphoneNotification(String alert, String title, String openPage, String extras) {
        super(alert, extras);
        this.title = title;
        this.openPage = openPage;
    }

    public static WinphoneNotification.Builder newBuilder() {
        return new WinphoneNotification.Builder();
    }

    public static WinphoneNotification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }

    public String getPlatform() {
        return "winphone";
    }

    public JsonElement toJSON() {
        JsonObject json = super.toJSON().getAsJsonObject();
        if(null != this.title) {
            json.add("title", new JsonPrimitive(this.title));
        }

        if(null != this.openPage) {
            json.add("_open_page", new JsonPrimitive(this.openPage));
        }

        return json;
    }

    public static class Builder extends com.sevenga.push.push.model.notification.PlatformNotification.Builder<WinphoneNotification> {
        private String title;
        private String openPage;

        public Builder() {
        }

        public WinphoneNotification.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public WinphoneNotification.Builder setOpenPage(String openPage) {
            this.openPage = openPage;
            return this;
        }

        public WinphoneNotification.Builder setAlert(String alert) {
            this.alert = alert;
            return this;
        }

//        public WinphoneNotification.Builder addExtra(String key, String value) {
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
//        public WinphoneNotification.Builder addExtras(Map<String, String> extras) {
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
//        public WinphoneNotification.Builder addExtra(String key, Number value) {
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
//        public WinphoneNotification.Builder addExtra(String key, Boolean value) {
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
//        public WinphoneNotification.Builder addExtra(String key, JsonObject value) {
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

        public WinphoneNotification build() {
            //return new WinphoneNotification(this.alert, this.title, this.openPage, this.extrasBuilder, this.numberExtrasBuilder, this.booleanExtrasBuilder, this.jsonExtrasBuilder);
            return new WinphoneNotification(this.alert, this.title, this.openPage, extras);
        }
    }
}
