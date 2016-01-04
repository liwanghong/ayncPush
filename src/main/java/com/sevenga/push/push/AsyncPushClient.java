package com.sevenga.push.push;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.push.model.PushPayload;
import com.sevenga.push.utils.Preconditions;
import com.sevenga.push.utils.StringUtils;
import org.apache.http.concurrent.BasicFuture;

/**
 * Created by lizi on 15/9/14.
 */
public class AsyncPushClient {
    public static final String HOST_NAME = "api.jpush.cn";
    public static final String PUSH_PATH = "/v3/push";
    public static final String PUSH_VALIDATE_PATH = "/v3/push/validate";
    public static final String SCHEME = "https";

    private AsyncHttpClient httpAsyncClient;
    private JsonParser _jsonParser;
    private boolean _apnsProduction;
    private long _timeToLive;
    private boolean _globalSettingEnabled;

    public AsyncPushClient(AsyncHttpClient httpAsyncClient)
    {
        this._jsonParser = new JsonParser();
        this._apnsProduction = true;
        this._timeToLive = 86400L;
        this._globalSettingEnabled = false;
        this.httpAsyncClient = httpAsyncClient;
    }

    public BasicFuture<PushResult> sendPush(PushPayload pushPayload) {
        Preconditions.checkArgument(null != pushPayload, "pushPayload should not be null");
        if(this._globalSettingEnabled) {
            pushPayload.resetOptionsTimeToLive(this._timeToLive);
            pushPayload.resetOptionsApnsProduction(this._apnsProduction);
        }

        BasicFuture<PushResult> futureResult = new BasicFuture<PushResult>(null);
        this.httpAsyncClient.sendPost(
                this.HOST_NAME,
                this.PUSH_PATH,
                pushPayload.toString(),
                new PushRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<PushResult> sendPushValidate(PushPayload pushPayload) {
        Preconditions.checkArgument(null != pushPayload, "pushPayload should not be null");
        if(this._globalSettingEnabled) {
            pushPayload.resetOptionsTimeToLive(this._timeToLive);
            pushPayload.resetOptionsApnsProduction(this._apnsProduction);
        }

        BasicFuture<PushResult> futureResult = new BasicFuture<PushResult>(null);
        this.httpAsyncClient.sendPost(
                this.HOST_NAME,
                this.PUSH_VALIDATE_PATH,
                pushPayload.toString(),
                new PushRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<PushResult> sendPush(String payloadString) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(payloadString), "pushPayload should not be empty");

        try {
            this._jsonParser.parse(payloadString);
        } catch (JsonParseException var3) {
            Preconditions.checkArgument(false, "payloadString should be a valid JSON string.");
        }

        BasicFuture<PushResult> futureResult = new BasicFuture<PushResult>(null);
        this.httpAsyncClient.sendPost(
                this.HOST_NAME,
                this.PUSH_PATH,
                payloadString,
                new PushRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<PushResult> sendPushValidate(String payloadString) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(payloadString), "pushPayload should not be empty");

        try {
            this._jsonParser.parse(payloadString);
        } catch (JsonParseException var3) {
            Preconditions.checkArgument(false, "payloadString should be a valid JSON string.");
        }

        BasicFuture<PushResult> futureResult = new BasicFuture<PushResult>(null);
        this.httpAsyncClient.sendPost(
                this.HOST_NAME,
                this.PUSH_VALIDATE_PATH,
                payloadString,
                new PushRequestCallback(futureResult));
        return futureResult;
    }
}
