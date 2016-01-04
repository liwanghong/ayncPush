package com.sevenga.push.push.model;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.push.model.audience.Audience;
import com.sevenga.push.push.model.notification.Notification;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class PushPayload implements PushModel {
    private static final String PLATFORM = "platform";
    private static final String AUDIENCE = "audience";
    private static final String NOTIFICATION = "notification";
    private static final String MESSAGE = "message";
    private static final String OPTIONS = "options";
    private static final int MAX_GLOBAL_ENTITY_LENGTH = 1200;
    private static final int MAX_IOS_PAYLOAD_LENGTH = 220;
    private static Gson _gson = new Gson();
    private final Platform platform;
    private final Audience audience;
    private final Notification notification;
    private final Message message;
    private Options options;

    private PushPayload(Platform platform, Audience audience, Notification notification, Message message, Options options) {
        this.platform = platform;
        this.audience = audience;
        this.notification = notification;
        this.message = message;
        this.options = options;
    }

    public static PushPayload.Builder newBuilder() {
        return new PushPayload.Builder();
    }

    public static PushPayload alertAll(String alert) {
        return (new PushPayload.Builder()).setPlatform(Platform.all()).setAudience(Audience.all()).setNotification(Notification.alert(alert)).build();
    }

    public static PushPayload messageAll(String msgContent) {
        return (new PushPayload.Builder()).setPlatform(Platform.all()).setAudience(Audience.all()).setMessage(Message.content(msgContent)).build();
    }

    public static PushPayload fromJSON(String payloadString) {
        return (PushPayload)_gson.fromJson(payloadString, PushPayload.class);
    }

    public void resetOptionsApnsProduction(boolean apnsProduction) {
        if(null == this.options) {
            this.options = Options.newBuilder().setApnsProduction(apnsProduction).build();
        } else {
            this.options.setApnsProduction(apnsProduction);
        }

    }

    public void resetOptionsTimeToLive(long timeToLive) {
        if(null == this.options) {
            this.options = Options.newBuilder().setTimeToLive(timeToLive).build();
        } else {
            this.options.setTimeToLive(timeToLive);
        }

    }

    public int getSendno() {
        return null != this.options?this.options.getSendno():0;
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if(null != this.platform) {
            json.add("platform", this.platform.toJSON());
        }

        if(null != this.audience) {
            json.add("audience", this.audience.toJSON());
        }

        if(null != this.notification) {
            json.add("notification", this.notification.toJSON());
        }

        if(null != this.message) {
            json.add("message", this.message.toJSON());
        }

        if(null != this.options) {
            json.add("options", this.options.toJSON());
        }

        return json;
    }

    public boolean isGlobalExceedLength() {
        int messageLength = 0;
        JsonObject payload = (JsonObject)this.toJSON();
        JsonObject notification;
        if(payload.has("message")) {
            notification = payload.getAsJsonObject("message");
            messageLength = notification.toString().getBytes().length;
        }

        if(!payload.has("notification")) {
            return messageLength > 1200;
        } else {
            notification = payload.getAsJsonObject("notification");
            if(notification.has("android")) {
                JsonObject android = notification.getAsJsonObject("android");
                int androidLength = android.toString().getBytes().length;
                return androidLength + messageLength > 1200;
            } else {
                return false;
            }
        }
    }

    public boolean isIosExceedLength() {
        JsonObject payload = (JsonObject)this.toJSON();
        if(payload.has("notification")) {
            JsonObject notification = payload.getAsJsonObject("notification");
            if(notification.has("ios")) {
                JsonObject alert1 = notification.getAsJsonObject("ios");
                return alert1.toString().getBytes().length > 220;
            }

            if(notification.has("alert")) {
                String alert = notification.get("alert").getAsString();
                JsonObject ios = new JsonObject();
                ios.add("alert", new JsonPrimitive(alert));
                return ios.toString().getBytes().length > 220;
            }
        }

        return false;
    }

    public String toString() {
        return _gson.toJson(this.toJSON());
    }

    public static class Builder {
        private Platform platform = null;
        private Audience audience = null;
        private Notification notification = null;
        private Message message = null;
        private Options options = null;

        public Builder() {
        }

        public PushPayload.Builder setPlatform(Platform platform) {
            this.platform = platform;
            return this;
        }

        public PushPayload.Builder setAudience(Audience audience) {
            this.audience = audience;
            return this;
        }

        public PushPayload.Builder setNotification(Notification notification) {
            this.notification = notification;
            return this;
        }

        public PushPayload.Builder setMessage(Message message) {
            this.message = message;
            return this;
        }

        public PushPayload.Builder setOptions(Options options) {
            this.options = options;
            return this;
        }

        public PushPayload build() {
            Preconditions.checkArgument(null != this.audience && null != this.platform, "audience and platform both should be set.");
            Preconditions.checkArgument(null != this.notification || null != this.message, "notification or message should be set at least one.");
            if(null == this.options) {
                this.options = Options.sendno();
            }

            return new PushPayload(this.platform, this.audience, this.notification, this.message, this.options);
        }
    }
}
