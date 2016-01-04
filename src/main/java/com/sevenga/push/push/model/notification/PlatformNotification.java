package com.sevenga.push.push.model.notification;

/**
 * Created by lizi on 15/9/8.
 */
import com.google.gson.JsonParser;
import com.sevenga.push.push.model.PushModel;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlatformNotification implements PushModel {
    public static final String ALERT = "alert";
    private static final String EXTRAS = "extras";
    protected static final Logger LOG = LoggerFactory.getLogger(PlatformNotification.class);
    private String alert;
    private String extras;
//    private final Map<String, String> extras;
//    private final Map<String, Number> numberExtras;
//    private final Map<String, Boolean> booleanExtras;
//    private final Map<String, JsonObject> jsonExtras;

//    public PlatformNotification(String alert, Map<String, String> extras, Map<String, Number> numberExtras, Map<String, Boolean> booleanExtras, Map<String, JsonObject> jsonExtras) {
//        this.alert = alert;
//        this.extras = extras;
//        this.numberExtras = numberExtras;
//        this.booleanExtras = booleanExtras;
//        this.jsonExtras = jsonExtras;
//    }
    public PlatformNotification(String alert, String extras) {
        this.alert = alert;
        this.extras = extras;
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if(null != this.alert) {
            json.add("alert", new JsonPrimitive(this.alert));
        }

        if (null != this.extras)
        {
            JsonParser parser = new JsonParser();
            JsonObject extraJson = (JsonObject)parser.parse(this.extras);
            json.add("extras", extraJson);
        }

//        JsonObject extrasObject = null;
//        if(null != this.extras || null != this.numberExtras || null != this.booleanExtras || null != this.jsonExtras) {
//            extrasObject = new JsonObject();
//        }
//
//        String value;
//        Iterator i$;
//        String key;
//        if(null != this.extras) {
//            value = null;
//            i$ = this.extras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                value = (String)this.extras.get(key);
//                if(null != value) {
//                    extrasObject.add(key, new JsonPrimitive(value));
//                }
//            }
//        }
//
//        if(null != this.numberExtras) {
//            value = null;
//            i$ = this.numberExtras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                Number value1 = (Number)this.numberExtras.get(key);
//                if(null != value1) {
//                    extrasObject.add(key, new JsonPrimitive(value1));
//                }
//            }
//        }
//
//        if(null != this.booleanExtras) {
//            value = null;
//            i$ = this.booleanExtras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                Boolean value2 = (Boolean)this.booleanExtras.get(key);
//                if(null != value2) {
//                    extrasObject.add(key, new JsonPrimitive(value2));
//                }
//            }
//        }
//
//        if(null != this.jsonExtras) {
//            value = null;
//            i$ = this.jsonExtras.keySet().iterator();
//
//            while(i$.hasNext()) {
//                key = (String)i$.next();
//                JsonObject value3 = (JsonObject)this.jsonExtras.get(key);
//                if(null != value3) {
//                    extrasObject.add(key, value3);
//                }
//            }
//        }
//
//        if(null != this.extras || null != this.numberExtras || null != this.booleanExtras || null != this.jsonExtras) {
//            json.add("extras", extrasObject);
//        }

        return json;
    }

    protected String getAlert() {
        return this.alert;
    }

    protected void setAlert(String alert) {
        this.alert = alert;
    }

    protected abstract String getPlatform();

    protected abstract static class Builder<T> {
        protected String alert;
        protected String extras;
//        protected Map<String, String> extrasBuilder;
//        protected Map<String, Number> numberExtrasBuilder;
//        protected Map<String, Boolean> booleanExtrasBuilder;
//        protected Map<String, JsonObject> jsonExtrasBuilder;

        protected Builder() {
        }

        public abstract PlatformNotification.Builder<T> setAlert(String var1);

        public PlatformNotification.Builder<T> setExtras(String extras)
        {
            this.extras = extras;
            return this;
        }


//        public PlatformNotification.Builder<T> addExtra(String key, String value) {
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
//        public PlatformNotification.Builder<T> addExtras(Map<String, String> extras) {
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
//        public PlatformNotification.Builder<T> addExtra(String key, Number value) {
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
//        public PlatformNotification.Builder<T> addExtra(String key, Boolean value) {
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
//        public PlatformNotification.Builder<T> addExtra(String key, JsonObject value) {
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

        public abstract T build();
    }
}
