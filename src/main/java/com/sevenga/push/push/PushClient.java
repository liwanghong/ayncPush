package com.sevenga.push.push;

import com.sevenga.push.common.ServiceHelper;
import com.sevenga.push.common.connection.HttpProxy;
import com.sevenga.push.common.connection.NativeHttpClient;
import com.sevenga.push.common.resp.APIConnectionException;
import com.sevenga.push.common.resp.APIRequestException;
import com.sevenga.push.common.resp.BaseResult;
import com.sevenga.push.common.resp.ResponseWrapper;
import com.sevenga.push.push.model.PushPayload;
import com.sevenga.push.utils.Preconditions;
import com.sevenga.push.utils.StringUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class PushClient {
    public static final String HOST_NAME_SSL = "https://api.jpush.cn";
    public static final String PUSH_PATH = "/v3/push";
    public static final String PUSH_VALIDATE_PATH = "/v3/push/validate";
    private final NativeHttpClient _httpClient;
    private JsonParser _jsonParser;
    private boolean _apnsProduction;
    private long _timeToLive;
    private boolean _globalSettingEnabled;
    private String _baseUrl;

    public PushClient(String masterSecret, String appKey) {
        this(masterSecret, appKey, 3);
    }

    public PushClient(String masterSecret, String appKey, int maxRetryTimes) {
        this(masterSecret, appKey, maxRetryTimes, (HttpProxy)null);
    }

    public PushClient(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy) {
        this._jsonParser = new JsonParser();
        this._apnsProduction = true;
        this._timeToLive = 86400L;
        this._globalSettingEnabled = false;
        ServiceHelper.checkBasic(appKey, masterSecret);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        this._baseUrl = "https://api.jpush.cn";
        this._httpClient = new NativeHttpClient(authCode, maxRetryTimes, proxy);
    }

    public PushClient(String masterSecret, String appKey, boolean apnsProduction, long timeToLive) {
        this(masterSecret, appKey);
        this._apnsProduction = apnsProduction;
        this._timeToLive = timeToLive;
        this._globalSettingEnabled = true;
    }

    public void setDefaults(boolean apnsProduction, long timeToLive) {
        this._apnsProduction = apnsProduction;
        this._timeToLive = timeToLive;
        this._globalSettingEnabled = true;
    }

    public void setBaseUrl(String baseUrl) {
        this._baseUrl = baseUrl;
    }

    public PushResult sendPush(PushPayload pushPayload) throws APIConnectionException, APIRequestException {
        Preconditions.checkArgument(null != pushPayload, "pushPayload should not be null");
        if(this._globalSettingEnabled) {
            pushPayload.resetOptionsTimeToLive(this._timeToLive);
            pushPayload.resetOptionsApnsProduction(this._apnsProduction);
        }

        ResponseWrapper response = this._httpClient.sendPost(this._baseUrl + "/v3/push", pushPayload.toString());
        return (PushResult)BaseResult.fromResponse(response, PushResult.class);
    }

    public PushResult sendPushValidate(PushPayload pushPayload) throws APIConnectionException, APIRequestException {
        Preconditions.checkArgument(null != pushPayload, "pushPayload should not be null");
        if(this._globalSettingEnabled) {
            pushPayload.resetOptionsTimeToLive(this._timeToLive);
            pushPayload.resetOptionsApnsProduction(this._apnsProduction);
        }

        ResponseWrapper response = this._httpClient.sendPost(this._baseUrl + "/v3/push/validate", pushPayload.toString());
        return (PushResult)BaseResult.fromResponse(response, PushResult.class);
    }

    public PushResult sendPush(String payloadString) throws APIConnectionException, APIRequestException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(payloadString), "pushPayload should not be empty");

        try {
            this._jsonParser.parse(payloadString);
        } catch (JsonParseException var3) {
            Preconditions.checkArgument(false, "payloadString should be a valid JSON string.");
        }

        ResponseWrapper response = this._httpClient.sendPost(this._baseUrl + "/v3/push", payloadString);
        return (PushResult)BaseResult.fromResponse(response, PushResult.class);
    }

    public PushResult sendPushValidate(String payloadString) throws APIConnectionException, APIRequestException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(payloadString), "pushPayload should not be empty");

        try {
            this._jsonParser.parse(payloadString);
        } catch (JsonParseException var3) {
            Preconditions.checkArgument(false, "payloadString should be a valid JSON string.");
        }

        ResponseWrapper response = this._httpClient.sendPost(this._baseUrl + "/v3/push/validate", payloadString);
        return (PushResult)BaseResult.fromResponse(response, PushResult.class);
    }
}
