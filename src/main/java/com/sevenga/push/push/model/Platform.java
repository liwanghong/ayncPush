package com.sevenga.push.push.model;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.common.DeviceType;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Platform implements PushModel {
    private static final String ALL = "all";
    private final boolean all;
    private final Set<DeviceType> deviceTypes;

    private Platform(boolean all, Set<DeviceType> deviceTypes) {
        this.all = all;
        this.deviceTypes = deviceTypes;
    }

    public static Platform.Builder newBuilder() {
        return new Platform.Builder();
    }

    public static Platform all() {
        return newBuilder().setAll(true).build();
    }

    public static Platform android() {
        return newBuilder().addDeviceType(DeviceType.Android).build();
    }

    public static Platform ios() {
        return newBuilder().addDeviceType(DeviceType.IOS).build();
    }

    public static Platform winphone() {
        return newBuilder().addDeviceType(DeviceType.WinPhone).build();
    }

    public static Platform android_ios() {
        return newBuilder().addDeviceType(DeviceType.Android).addDeviceType(DeviceType.IOS).build();
    }

    public static Platform android_winphone() {
        return newBuilder().addDeviceType(DeviceType.Android).addDeviceType(DeviceType.WinPhone).build();
    }

    public static Platform ios_winphone() {
        return newBuilder().addDeviceType(DeviceType.IOS).addDeviceType(DeviceType.WinPhone).build();
    }

    public boolean isAll() {
        return this.all;
    }

    public JsonElement toJSON() {
        if(this.all) {
            return new JsonPrimitive("all");
        } else {
            JsonArray json = new JsonArray();
            Iterator i$ = this.deviceTypes.iterator();

            while(i$.hasNext()) {
                DeviceType deviceType = (DeviceType)i$.next();
                json.add(new JsonPrimitive(deviceType.value()));
            }

            return json;
        }
    }

    public static class Builder {
        private boolean all;
        private Set<DeviceType> deviceTypes;

        public Builder() {
        }

        public Platform.Builder setAll(boolean all) {
            this.all = all;
            return this;
        }

        public Platform.Builder addDeviceType(DeviceType deviceType) {
            if(null == this.deviceTypes) {
                this.deviceTypes = new HashSet();
            }

            this.deviceTypes.add(deviceType);
            return this;
        }

        public Platform build() {
            Preconditions.checkArgument(!this.all || null == this.deviceTypes, "Since all is enabled, any platform should not be set.");
            Preconditions.checkArgument(this.all || null != this.deviceTypes, "No any deviceType is set.");
            return new Platform(this.all, this.deviceTypes);
        }
    }
}
