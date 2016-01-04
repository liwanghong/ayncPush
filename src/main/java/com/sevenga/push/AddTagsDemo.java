package com.sevenga.push;

import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.common.connection.AsyncHttpClientFactory;
import com.sevenga.push.device.AsyncDeviceClient;
import com.sevenga.push.utils.ClearTagCallback;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lizi on 15/9/15.
 */
public class AddTagsDemo {
    public static void main(String[] args)
    {
        AsyncHttpClient httpClient = AsyncHttpClientFactory.createAsyncHttpClient(
                "yourMasterSecret",
                "yourAppKey",
                6);

        AsyncDeviceClient deviceClient = new AsyncDeviceClient(httpClient);

        Set<String> toAddTags = new HashSet<String>();
        toAddTags.add("alliance_2146437838_kingdom_1000");
        toAddTags.add("1000");
        Set<String> toRemoveTags = new HashSet<String>();
        ClearTagCallback clearTagCallback = new ClearTagCallback(deviceClient, toAddTags, toRemoveTags, "2146437219");
        deviceClient.getAliasDeviceList(
                "2146437219",
                "android,ios",
                clearTagCallback
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
