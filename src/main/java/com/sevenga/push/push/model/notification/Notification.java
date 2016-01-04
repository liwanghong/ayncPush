package com.sevenga.push.push.model.notification;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.push.model.PushModel;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Notification implements PushModel {
    private final String alert;
    private final Set<PlatformNotification> notifications;

    private Notification(String alert, Set<PlatformNotification> notifications) {
        this.alert = alert;
        this.notifications = notifications;
    }

    public static Notification.Builder newBuilder() {
        return new Notification.Builder();
    }

    public static Notification alert(String alert) {
        return newBuilder().setAlert(alert).build();
    }

//    public static Notification android(String alert, String title, Map<String, String> extras) {
//        return newBuilder().addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).setTitle(title).addExtras(extras).build()).build();
//    }
//
//    public static Notification ios(String alert, Map<String, String> extras) {
//        return newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(extras).build()).build();
//    }

    public static Notification android(String alert, String title, String extras) {
        return newBuilder().addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).setTitle(title).setExtras(extras).build()).build();
    }

    public static Notification ios(String alert, String extras) {
        return newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(alert).setExtras(extras).build()).build();
    }

    public static Notification ios_auto_badge() {
        return newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert("").autoBadge().build()).build();
    }

    public static Notification ios_set_badge(int badge) {
        return newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert("").setBadge(badge).build()).build();
    }

    public static Notification ios_incr_badge(int badge) {
        return newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert("").incrBadge(badge).build()).build();
    }

//    public static Notification winphone(String alert, Map<String, String> extras) {
//        return newBuilder().addPlatformNotification(WinphoneNotification.newBuilder().setAlert(alert).addExtras(extras).build()).build();
//    }

    public static Notification winphone(String alert, String extras) {
        return newBuilder().addPlatformNotification(WinphoneNotification.newBuilder().setAlert(alert).setExtras(extras).build()).build();
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if(null != this.alert) {
            json.add("alert", new JsonPrimitive(this.alert));
        }

        if(null != this.notifications) {
            Iterator i$ = this.notifications.iterator();

            while(i$.hasNext()) {
                PlatformNotification pn = (PlatformNotification)i$.next();
                if(this.alert != null && pn.getAlert() == null) {
                    pn.setAlert(this.alert);
                }

                Preconditions.checkArgument(null != pn.getAlert(), "For any platform notification, alert field is needed. It can be empty string.");
                json.add(pn.getPlatform(), pn.toJSON());
            }
        }

        return json;
    }

    public static class Builder {
        private String alert;
        private Set<PlatformNotification> builder;

        public Builder() {
        }

        public Notification.Builder setAlert(String alert) {
            this.alert = alert;
            return this;
        }

        public Notification.Builder addPlatformNotification(PlatformNotification notification) {
            if(null == this.builder) {
                this.builder = new HashSet();
            }

            this.builder.add(notification);
            return this;
        }

        public Notification build() {
            Preconditions.checkArgument(null != this.builder || null != this.alert, "No notification payload is set.");
            return new Notification(this.alert, this.builder);
        }
    }
}
