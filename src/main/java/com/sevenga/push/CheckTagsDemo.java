package com.sevenga.push;

import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.common.connection.AsyncHttpClientFactory;
import com.sevenga.push.device.AsyncDeviceClient;
import com.sevenga.push.device.TagAliasResult;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by lizi on 15/9/15.
 */
public class CheckTagsDemo {
    public static void main(String[] args)
    {
        AsyncHttpClient httpClient = AsyncHttpClientFactory.createAsyncHttpClient(
                "yourMasterSecret",
                "yourAppKey",
                6);

        AsyncDeviceClient deviceClient = new AsyncDeviceClient(httpClient);

        deviceClient.getDeviceTagAlias("040c03d87d8", new FutureCallback<TagAliasResult>() {
            public void completed(TagAliasResult tagAliasResult) {
                for (String tag: tagAliasResult.tags) {
                    System.out.println("tag " + tag);
                }
            }

            public void failed(Exception e) {

            }

            public void cancelled() {

            }
        });

        try{
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
