package com.sevenga.push.common.resp;

/**
 * Created by lizi on 15/9/7.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BaseResult implements IRateLimiting {
    public static final int ERROR_CODE_NONE = -1;
    public static final int ERROR_CODE_OK = 0;
    public static final String ERROR_MESSAGE_NONE = "None error message.";
    protected static final int RESPONSE_OK = 200;
    protected static Gson _gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
    private ResponseWrapper responseWrapper;

    public BaseResult() {
    }

    public void setResponseWrapper(ResponseWrapper responseWrapper) {
        this.responseWrapper = responseWrapper;
    }

    public String getOriginalContent() {
        return null != this.responseWrapper?this.responseWrapper.responseContent:null;
    }

    public boolean isResultOK() {
        return 200 == this.responseWrapper.responseCode;
    }

    public static <T extends BaseResult> T fromResponse(ResponseWrapper responseWrapper, Class<T> clazz) {
        T result = null;
        if(responseWrapper.isServerResponse()) {
            result = _gson.fromJson(responseWrapper.responseContent, clazz);
        } else {
            try {
                result = clazz.newInstance();
            } catch (InstantiationException var4) {
                var4.printStackTrace();
            } catch (IllegalAccessException var5) {
                var5.printStackTrace();
            }
        }

        result.setResponseWrapper(responseWrapper);
        return result;
    }

    public int getRateLimitQuota() {
        return null != this.responseWrapper?this.responseWrapper.rateLimitQuota:0;
    }

    public int getRateLimitRemaining() {
        return null != this.responseWrapper?this.responseWrapper.rateLimitRemaining:0;
    }

    public int getRateLimitReset() {
        return null != this.responseWrapper?this.responseWrapper.rateLimitReset:0;
    }

    public String toString() {
        return _gson.toJson(this);
    }
}
