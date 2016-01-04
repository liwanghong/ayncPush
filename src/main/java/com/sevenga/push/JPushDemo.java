package com.sevenga.push;

import com.google.gson.Gson;
import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.common.connection.AsyncHttpClientFactory;
import com.sevenga.push.push.AsyncPushClient;
import com.sevenga.push.push.model.Message;
import com.sevenga.push.push.model.Options;
import com.sevenga.push.push.model.Platform;
import com.sevenga.push.push.model.PushPayload;
import com.sevenga.push.push.model.audience.Audience;

/**
 * Created by lizi on 15/9/9.
 */
public class JPushDemo {
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Invalid param");
            //return;
            args = new String[] {"1048577"};
        }

        PushTip pushTip = new PushTip();
        pushTip.setId(9)
                .setPushType("combat")
                .setPushEn("Enemy attack")
                .setPushMusic("push")
                .setPushText("Enemy attack");
        AsyncHttpClient httpClient = AsyncHttpClientFactory.createAsyncHttpClient(
                "yourMasterKey",
                "yourAppKey",
                6);
        AsyncPushClient pushClient = new AsyncPushClient(httpClient);

        Gson gson = new Gson();


        for (String arg: args) {
            Options.Builder options = Options.newBuilder();
            options.setTimeToLive(60);
            PushPayload.Builder builder = PushPayload.newBuilder();
            builder.setPlatform(Platform.all());
            builder.setOptions(options.build());


            builder.setMessage(Message.content(gson.toJson(pushTip)));
            builder.setAudience(Audience.alias(arg));
            PushPayload pushPayLoad = builder.build();

            for (int i = 0; i < 100; i++) {
                pushClient.sendPush(pushPayLoad.toString());
            }
            try {
                Thread.sleep(20000);
            } catch (Exception e) {
            }
        }

        try {
            Thread.sleep(20000);
        } catch (Exception e) {
        }

        System.out.println("Finish");
    }
}
