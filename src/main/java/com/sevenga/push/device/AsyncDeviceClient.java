package com.sevenga.push.device;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sevenga.push.common.ServiceHelper;
import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.common.resp.*;
import com.sevenga.push.utils.Preconditions;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by lizi on 15/9/14.
 */
public class AsyncDeviceClient {
    public static final String HOST_NAME = "device.jpush.cn";
    public static final String DEVICES_PATH = "/v3/devices";
    public static final String TAGS_PATH = "/v3/tags";
    public static final String ALIASES_PATH = "/v3/aliases";
    private  AsyncHttpClient httpAsyncClient;

    public AsyncDeviceClient(AsyncHttpClient httpAsyncClient)
    {
        this.httpAsyncClient = httpAsyncClient;
    }

    public BasicFuture<TagAliasResult> getDeviceTagAlias(String registrationId, FutureCallback<TagAliasResult> callback) {
        String uri = DEVICES_PATH + "/" + registrationId;
        BasicFuture<TagAliasResult> futureResult = new BasicFuture<TagAliasResult>(callback);
        httpAsyncClient.sendGet(HOST_NAME, uri,
                new BaseRequestCallback<TagAliasResult>(futureResult, TagAliasResult.class));
        return futureResult;
    }

    public BasicFuture<DefaultResult> updateDeviceTagAlias(
            String registrationId,
            boolean clearAlias,
            boolean clearTag,
            FutureCallback<DefaultResult> callback) {
        Preconditions.checkArgument(clearAlias || clearTag, "It is not meaningful to do nothing.");
        String uri = DEVICES_PATH + "/" + registrationId;
        JsonObject top = new JsonObject();
        if(clearAlias) {
            top.addProperty("alias", "");
        }

        if(clearTag) {
            top.addProperty("tags", "");
        }

        BasicFuture<DefaultResult> futureResult = new BasicFuture<DefaultResult>(callback);
        httpAsyncClient.sendPost(HOST_NAME, uri, top.toString(), new DefaultRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<DefaultResult> updateDeviceTagAlias(
            String registrationId,
            String alias,
            Set<String> tagsToAdd,
            Set<String> tagsToRemove,
            FutureCallback<DefaultResult> callback)  {
        String uri = DEVICES_PATH + "/" + registrationId;
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

        BasicFuture<DefaultResult> futureResult = new BasicFuture<DefaultResult>(callback);
        httpAsyncClient.sendPost(HOST_NAME, uri, top.toString(), new DefaultRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<TagListResult> getTagList(FutureCallback<TagListResult> callback){
        BasicFuture<TagListResult> futureResult = new BasicFuture<TagListResult>(callback);
        httpAsyncClient.sendGet(HOST_NAME, TAGS_PATH,
                new BaseRequestCallback<TagListResult>(futureResult, TagListResult.class));
        return futureResult;
    }

    public BasicFuture<BooleanResult> isDeviceInTag(
            String theTag, String registrationID, FutureCallback<BooleanResult> callback)  {
        String uri =  TAGS_PATH + "/" + theTag + "/registration_ids/" + registrationID;
        BasicFuture<BooleanResult> futureResult = new BasicFuture<BooleanResult>(callback);
        httpAsyncClient.sendGet(HOST_NAME, uri,
                new BaseRequestCallback<BooleanResult>(futureResult, BooleanResult.class));
        return futureResult;
    }

    public BasicFuture<DefaultResult> addRemoveDevicesFromTag(
            String theTag, Set<String> toAddUsers, Set<String> toRemoveUsers, FutureCallback<DefaultResult> callback){
        String uri = TAGS_PATH + "/" + theTag;
        JsonObject top = new JsonObject();
        JsonObject registrationIds = new JsonObject();
        JsonArray response;
        Iterator i;
        String user;
        if(null != toAddUsers && toAddUsers.size() > 0) {
            response = new JsonArray();
            i = toAddUsers.iterator();

            while(i.hasNext()) {
                user = (String)i.next();
                response.add(new JsonPrimitive(user));
            }

            registrationIds.add("add", response);
        }

        if(null != toRemoveUsers && toRemoveUsers.size() > 0) {
            response = new JsonArray();
            i = toRemoveUsers.iterator();

            while(i.hasNext()) {
                user = (String)i.next();
                response.add(new JsonPrimitive(user));
            }

            registrationIds.add("remove", response);
        }

        top.add("registration_ids", registrationIds);

        BasicFuture<DefaultResult> futureResult = new BasicFuture<DefaultResult>(callback);
        httpAsyncClient.sendPost(HOST_NAME, uri, top.toString(), new DefaultRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<DefaultResult> deleteTag(String theTag, String platform){
        String uri =  TAGS_PATH + "/" + theTag;
        if(null != platform) {
            uri = uri + "?platform=" + platform;
        }

        BasicFuture<DefaultResult> futureResult = new BasicFuture<DefaultResult>(null);
        httpAsyncClient.sendDelete(HOST_NAME, uri, new DefaultRequestCallback(futureResult));
        return futureResult;
    }

    public  BasicFuture<AliasDeviceListResult> getAliasDeviceList(
            String alias, String platform, FutureCallback<AliasDeviceListResult> callback) {
        String uri = ALIASES_PATH + "/" + alias;
        if(null != platform) {
            uri = uri + "?platform=" + platform;
        }

        BasicFuture<AliasDeviceListResult> futureResult = new BasicFuture<AliasDeviceListResult>(callback);
        httpAsyncClient.sendGet(HOST_NAME, uri,
                new BaseRequestCallback<AliasDeviceListResult>(futureResult, AliasDeviceListResult.class));
        return futureResult;
    }

    public BasicFuture<DefaultResult> deleteAlias(String alias, String platform) throws APIConnectionException, APIRequestException {
        String uri = ALIASES_PATH + "/" + alias;
        if(null != platform) {
            uri = uri + "?platform=" + platform;
        }

        BasicFuture<DefaultResult> futureResult = new BasicFuture<DefaultResult>(null);
        httpAsyncClient.sendDelete(HOST_NAME, uri, new DefaultRequestCallback(futureResult));
        return futureResult;
    }
}
