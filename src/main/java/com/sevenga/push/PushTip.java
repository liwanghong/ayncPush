package com.sevenga.push;

/**
 * Created by lizi on 15/9/9.
 */
public class PushTip{
    private Integer id;
    private String pushType;
    private String pushText;
    private String pushMusic;
    private String pushEn;

    public Integer getId() {
        return id;
    }

    public PushTip setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPushType() {
        return pushType;
    }

    public PushTip setPushType(String pushType) {
        this.pushType = pushType;
        return this;
    }

    public String getPushText() {
        return pushText;
    }

    public PushTip setPushText(String pushText) {
        this.pushText = pushText;
        return this;
    }

    public String getPushMusic() {
        return pushMusic;
    }

    public PushTip setPushMusic(String pushMusic) {
        this.pushMusic = pushMusic;
        return this;
    }

    public String getPushEn() {
        return pushEn;
    }

    public PushTip setPushEn(String pushEn) {
        this.pushEn = pushEn;
        return this;
    }
}