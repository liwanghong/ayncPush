package com.sevenga.push.utils;

import com.sevenga.push.common.resp.DefaultResult;
import com.sevenga.push.device.AsyncDeviceClient;
import org.apache.http.concurrent.FutureCallback;

import java.util.Set;

/**
 * Created by lizi on 15/9/15.
 */
public class AddRemoveAllianceTagCallback implements FutureCallback<DefaultResult> {
    private String      registrationId;
    private String      alias;
    private Set<String> toAddTags;
    private Set<String> toRemoveTags;
    private AsyncDeviceClient deviceClient;

    public AddRemoveAllianceTagCallback(
            String registrationId,
            String alias,
            Set<String> toAddTags,
            Set<String> toRemoveTags,
            AsyncDeviceClient deviceClient) {
        this.registrationId = registrationId;
        this.alias = alias;
        this.toAddTags = toAddTags;
        this.toRemoveTags = toRemoveTags;
        this.deviceClient = deviceClient;
    }

    public void completed(DefaultResult defaultResult) {
        deviceClient.updateDeviceTagAlias(registrationId, alias, toAddTags, toRemoveTags, null);
    }

    public void failed(Exception e) {
    }

    public void cancelled() {

    }
}
