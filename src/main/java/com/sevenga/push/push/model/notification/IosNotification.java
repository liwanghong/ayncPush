package com.sevenga.push.push.model.notification;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.common.ServiceHelper;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IosNotification extends PlatformNotification {
    public static final String NOTIFICATION_IOS = "ios";
    private static final String DEFAULT_SOUND = "";
    private static final String DEFAULT_BADGE = "+1";
    private static final String BADGE = "badge";
    private static final String SOUND = "sound";
    private static final String CONTENT_AVAILABLE = "content-available";
    private static final String CATEGORY = "category";
    private static final String ALERT_VALID_BADGE = "Badge number should be 0~99999, and can be prefixed with + to add, - to minus";
    private final boolean soundDisabled;
    private final boolean badgeDisabled;
    private final String sound;
    private final String badge;
    private final boolean contentAvailable;
    private final String category;

//    private IosNotification(String alert, String sound, String badge, boolean contentAvailable, boolean soundDisabled, boolean badgeDisabled, String category, Map<String, String> extras, Map<String, Number> numberExtras, Map<String, Boolean> booleanExtras, Map<String, JsonObject> jsonExtras) {
//        super(alert, extras, numberExtras, booleanExtras, jsonExtras);
//        this.sound = sound;
//        this.badge = badge;
//        this.contentAvailable = contentAvailable;
//        this.soundDisabled = soundDisabled;
//        this.badgeDisabled = badgeDisabled;
//        this.category = category;
//    }

    private IosNotification(String alert, String sound, String badge, boolean contentAvailable, boolean soundDisabled, boolean badgeDisabled, String category, String extras) {
        super(alert, extras);
        this.sound = sound;
        this.badge = badge;
        this.contentAvailable = contentAvailable;
        this.soundDisabled = soundDisabled;
        this.badgeDisabled = badgeDisabled;
        this.category = category;
    }

    public static IosNotification.Builder newBuilder() {
        return new IosNotification.Builder();
    }

    public static IosNotification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }

    public String getPlatform() {
        return "ios";
    }

    public JsonElement toJSON() {
        JsonObject json = super.toJSON().getAsJsonObject();
        if(!this.badgeDisabled) {
            if(null != this.badge) {
                json.add("badge", new JsonPrimitive(this.badge));
            } else {
                json.add("badge", new JsonPrimitive("+1"));
            }
        }

        if(!this.soundDisabled) {
            if(null != this.sound) {
                json.add("sound", new JsonPrimitive(this.sound));
            } else {
                json.add("sound", new JsonPrimitive(""));
            }
        }

        if(this.contentAvailable) {
            json.add("content-available", new JsonPrimitive(Integer.valueOf(1)));
        }

        if(null != this.category) {
            json.add("category", new JsonPrimitive(this.category));
        }

        return json;
    }

    public static class Builder extends com.sevenga.push.push.model.notification.PlatformNotification.Builder<IosNotification> {
        private String sound;
        private String badge;
        private boolean contentAvailable = false;
        private boolean soundDisabled = false;
        private boolean badgeDisabled = false;
        private String category;

        public Builder() {
        }

        public IosNotification.Builder setSound(String sound) {
            this.sound = sound;
            return this;
        }

        public IosNotification.Builder disableSound() {
            this.soundDisabled = true;
            return this;
        }

        public IosNotification.Builder incrBadge(int badge) {
            if(!ServiceHelper.isValidIntBadge(Math.abs(badge))) {
                PlatformNotification.LOG.warn("Badge number should be 0~99999, and can be prefixed with + to add, - to minus");
                return this;
            } else {
                if(badge >= 0) {
                    this.badge = "+" + badge;
                } else {
                    this.badge = "" + badge;
                }

                return this;
            }
        }

        public IosNotification.Builder setBadge(int badge) {
            if(!ServiceHelper.isValidIntBadge(badge)) {
                PlatformNotification.LOG.warn("Badge number should be 0~99999, and can be prefixed with + to add, - to minus");
                return this;
            } else {
                this.badge = "" + badge;
                return this;
            }
        }

        public IosNotification.Builder autoBadge() {
            return this.incrBadge(1);
        }

        public IosNotification.Builder disableBadge() {
            this.badgeDisabled = true;
            return this;
        }

        public IosNotification.Builder setContentAvailable(boolean contentAvailable) {
            this.contentAvailable = contentAvailable;
            return this;
        }

        public IosNotification.Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public IosNotification.Builder setAlert(String alert) {
            this.alert = alert;
            return this;
        }

//        public IosNotification.Builder addExtra(String key, String value) {
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
//        public IosNotification.Builder addExtras(Map<String, String> extras) {
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
//        public IosNotification.Builder addExtra(String key, Number value) {
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
//        public IosNotification.Builder addExtra(String key, Boolean value) {
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
//        public IosNotification.Builder addExtra(String key, JsonObject value) {
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

        public IosNotification build() {
            //return new IosNotification(this.alert, this.sound, this.badge, this.contentAvailable, this.soundDisabled, this.badgeDisabled, this.category, this.extrasBuilder, this.numberExtrasBuilder, this.booleanExtrasBuilder, this.jsonExtrasBuilder);
            return new IosNotification(this.alert, this.sound, this.badge, this.contentAvailable, this.soundDisabled, this.badgeDisabled, this.category, this.extras);
        }
    }
}
