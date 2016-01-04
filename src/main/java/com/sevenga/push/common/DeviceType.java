package com.sevenga.push.common;

/**
 * Created by lizi on 15/9/7.
 */
public enum DeviceType {
    Android("android"),
    IOS("ios"),
    WinPhone("winphone");

    private final String value;

    private DeviceType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
