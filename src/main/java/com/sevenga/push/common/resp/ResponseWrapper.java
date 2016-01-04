package com.sevenga.push.common.resp;

/**
 * Created by lizi on 15/9/7.
 */
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseWrapper.class);
    private static final int RESPONSE_CODE_NONE = -1;
    private static Gson _gson = new Gson();
    public int responseCode = -1;
    public String responseContent;
    public ResponseWrapper.ErrorObject error;
    public int rateLimitQuota;
    public int rateLimitRemaining;
    public int rateLimitReset;

    public ResponseWrapper() {
    }

    public void setRateLimit(String quota, String remaining, String reset) {
        if(null != quota) {
            try {
                this.rateLimitQuota = Integer.parseInt(quota);
                this.rateLimitRemaining = Integer.parseInt(remaining);
                this.rateLimitReset = Integer.parseInt(reset);
                LOG.debug("JPush API Rate Limiting params - quota:" + quota + ", remaining:" + remaining + ", reset:" + reset);
            } catch (NumberFormatException var5) {
                LOG.debug("Unexpected - parse rate limiting headers error.");
            }

        }
    }

    public void setErrorObject() {
        this.error = (ResponseWrapper.ErrorObject)_gson.fromJson(this.responseContent, ResponseWrapper.ErrorObject.class);
    }

    public boolean isServerResponse() {
        return this.responseCode == 200?true:this.responseCode > 0 && null != this.error && this.error.error.code > 0;
    }

    public String toString() {
        return _gson.toJson(this);
    }

    public class ErrorEntity {
        public int code;
        public String message;

        public ErrorEntity() {
        }

        public String toString() {
            return ResponseWrapper._gson.toJson(this);
        }
    }

    public class ErrorObject {
        public long msg_id;
        public ResponseWrapper.ErrorEntity error;

        public ErrorObject() {
        }
    }
}
