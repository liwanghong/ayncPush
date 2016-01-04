package com.sevenga.push.push.model.audience;

/**
 * Created by lizi on 15/9/8.
 */
public enum AudienceType {
    TAG("tag"),
    TAG_AND("tag_and"),
    ALIAS("alias"),
    SEGMENT("segment"),
    REGISTRATION_ID("registration_id");

    private final String value;

    private AudienceType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
