package com.sevenga.push.utils;

import com.sevenga.push.device.AliasDeviceListResult;
import com.sevenga.push.device.AsyncDeviceClient;
import org.apache.http.concurrent.FutureCallback;

import java.util.Set;

/**
 * Created by lizi on 15/9/15.
 */
public class ClearTagCallback implements FutureCallback<AliasDeviceListResult>{
    private AsyncDeviceClient deviceClient;
    private Set<String>   toAddTags;
    private Set<String>   toRemoveTags;
    private String   alias;

    public ClearTagCallback(
            AsyncDeviceClient deviceClient,
            Set<String> toAddTags,
            Set<String> toRemoveTags,
            String alias) {
        this.deviceClient = deviceClient;
        this.toAddTags = toAddTags;
        this.toRemoveTags = toRemoveTags;
        this.alias = alias;
    }

    public void completed(AliasDeviceListResult aliasDeviceListResult) {
        for (String id: aliasDeviceListResult.registration_ids)
        {
            System.out.println("register id " + id);
            deviceClient.updateDeviceTagAlias(id, false, true,
                    new AddRemoveAllianceTagCallback(id, alias, toAddTags, toRemoveTags, deviceClient));
        }
    }

    public void failed(Exception e) {
    }

    public void cancelled() {

    }
}