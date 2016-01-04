package com.sevenga.push.device;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.common.ServiceHelper;
import com.sevenga.push.common.connection.HttpProxy;
import com.sevenga.push.common.connection.NativeHttpClient;
import com.sevenga.push.common.resp.APIConnectionException;
import com.sevenga.push.common.resp.APIRequestException;
import com.sevenga.push.common.resp.BaseResult;
import com.sevenga.push.common.resp.BooleanResult;
import com.sevenga.push.common.resp.DefaultResult;
import com.sevenga.push.common.resp.ResponseWrapper;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import java.util.Set;

public class DeviceClient {
    public static final String HOST_NAME_SSL = "https://device.jpush.cn";
    public static final String DEVICES_PATH = "/v3/devices";
    public static final String TAGS_PATH = "/v3/tags";
    public static final String ALIASES_PATH = "/v3/aliases";
    private final NativeHttpClient _httpClient;

    public DeviceClient(String masterSecret, String appKey) {
        this(masterSecret, appKey, 3);
    }

    public DeviceClient(String masterSecret, String appKey, int maxRetryTimes) {
        this(masterSecret, appKey, maxRetryTimes, (HttpProxy)null);
    }

    public DeviceClient(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy) {
        ServiceHelper.checkBasic(appKey, masterSecret);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        this._httpClient = new NativeHttpClient(authCode, maxRetryTimes, proxy);
    }

    public TagAliasResult getDeviceTagAlias(String registrationId) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/devices/" + registrationId;
        ResponseWrapper response = this._httpClient.sendGet(url);
        return (TagAliasResult)BaseResult.fromResponse(response, TagAliasResult.class);
    }

    public DefaultResult updateDeviceTagAlias(String registrationId, boolean clearAlias, boolean clearTag) throws APIConnectionException, APIRequestException {
        Preconditions.checkArgument(clearAlias || clearTag, "It is not meaningful to do nothing.");
        String url = "https://device.jpush.cn/v3/devices/" + registrationId;
        JsonObject top = new JsonObject();
        if(clearAlias) {
            top.addProperty("alias", "");
        }

        if(clearTag) {
            top.addProperty("tags", "");
        }

        ResponseWrapper response = this._httpClient.sendPost(url, top.toString());
        return DefaultResult.fromResponse(response);
    }

    public DefaultResult updateDeviceTagAlias(String registrationId, String alias, Set<String> tagsToAdd, Set<String> tagsToRemove) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/devices/" + registrationId;
        JsonObject top = new JsonObject();
        if(null != alias) {
            top.addProperty("alias", alias);
        }

        JsonObject tagObject = new JsonObject();
        JsonArray tagsAdd = ServiceHelper.fromSet(tagsToAdd);
        if(tagsAdd.size() > 0) {
            tagObject.add("add", tagsAdd);
        }

        JsonArray tagsRemove = ServiceHelper.fromSet(tagsToRemove);
        if(tagsRemove.size() > 0) {
            tagObject.add("remove", tagsRemove);
        }

        if(tagObject.entrySet().size() > 0) {
            top.add("tags", tagObject);
        }

        ResponseWrapper response = this._httpClient.sendPost(url, top.toString());
        return DefaultResult.fromResponse(response);
    }

    public TagListResult getTagList() throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/tags/";
        ResponseWrapper response = this._httpClient.sendGet(url);
        return (TagListResult)TagListResult.fromResponse(response, TagListResult.class);
    }

    public BooleanResult isDeviceInTag(String theTag, String registrationID) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/tags/" + theTag + "/registration_ids/" + registrationID;
        ResponseWrapper response = this._httpClient.sendGet(url);
        return (BooleanResult)BaseResult.fromResponse(response, BooleanResult.class);
    }

    public DefaultResult addRemoveDevicesFromTag(String theTag, Set<String> toAddUsers, Set<String> toRemoveUsers) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/tags/" + theTag;
        JsonObject top = new JsonObject();
        JsonObject registrationIds = new JsonObject();
        JsonArray response;
        Iterator i$;
        String user;
        if(null != toAddUsers && toAddUsers.size() > 0) {
            response = new JsonArray();
            i$ = toAddUsers.iterator();

            while(i$.hasNext()) {
                user = (String)i$.next();
                response.add(new JsonPrimitive(user));
            }

            registrationIds.add("add", response);
        }

        if(null != toRemoveUsers && toRemoveUsers.size() > 0) {
            response = new JsonArray();
            i$ = toRemoveUsers.iterator();

            while(i$.hasNext()) {
                user = (String)i$.next();
                response.add(new JsonPrimitive(user));
            }

            registrationIds.add("remove", response);
        }

        top.add("registration_ids", registrationIds);
        ResponseWrapper response1 = this._httpClient.sendPost(url, top.toString());
        return DefaultResult.fromResponse(response1);
    }

    public DefaultResult deleteTag(String theTag, String platform) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/tags/" + theTag;
        if(null != platform) {
            url = url + "?platform=" + platform;
        }

        ResponseWrapper response = this._httpClient.sendDelete(url);
        return DefaultResult.fromResponse(response);
    }

    public AliasDeviceListResult getAliasDeviceList(String alias, String platform) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/aliases/" + alias;
        if(null != platform) {
            url = url + "?platform=" + platform;
        }

        ResponseWrapper response = this._httpClient.sendGet(url);
        return (AliasDeviceListResult)BaseResult.fromResponse(response, AliasDeviceListResult.class);
    }

    public DefaultResult deleteAlias(String alias, String platform) throws APIConnectionException, APIRequestException {
        String url = "https://device.jpush.cn/v3/aliases/" + alias;
        if(null != platform) {
            url = url + "?platform=" + platform;
        }

        ResponseWrapper response = this._httpClient.sendDelete(url);
        return DefaultResult.fromResponse(response);
    }
}
