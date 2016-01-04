package com.sevenga.push;

import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.common.connection.AsyncHttpClientFactory;
import com.sevenga.push.device.AliasDeviceListResult;
import com.sevenga.push.device.AsyncDeviceClient;
import org.apache.http.concurrent.FutureCallback;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lizi on 15/9/15.
 */
public class RemoveAllianceTagCallback {
    public static void main(String[] args)
    {
        AsyncHttpClient httpClient = AsyncHttpClientFactory.createAsyncHttpClient(
                "d1d163e380f908abff84fac6",
                "c9e733df302c5ba535d64368",
                6);

        final AsyncDeviceClient deviceClient = new AsyncDeviceClient(httpClient);

        final Set<String> toAddTags = new HashSet<String>();
        final Set<String> toRemoveTags = new HashSet<String>();
        toRemoveTags.add("2146435073");
        deviceClient.getAliasDeviceList(
                "2146441165",
                "android,ios",
                new FutureCallback<AliasDeviceListResult>() {
                    public void completed(AliasDeviceListResult aliasDeviceListResult) {
                        for (String id: aliasDeviceListResult.registration_ids) {
                            deviceClient.updateDeviceTagAlias(id, "2146441165", toAddTags, toRemoveTags, null);
                        }
                    }

                    public void failed(Exception e) {

                    }

                    public void cancelled() {

                    }
                }
        );

        try{
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
