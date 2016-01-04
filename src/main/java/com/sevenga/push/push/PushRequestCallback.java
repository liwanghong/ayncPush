package com.sevenga.push.push;

import com.sevenga.push.common.resp.BaseResult;
import com.sevenga.push.common.resp.ResponseWrapper;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by lizi on 15/9/14.
 */
public class PushRequestCallback implements FutureCallback<ResponseWrapper> {
    private BasicFuture<PushResult> futureResult;

    public PushRequestCallback(BasicFuture<PushResult> futureResult) {
        this.futureResult = futureResult;
    }

    public void completed(ResponseWrapper response)
    {
        futureResult.completed(BaseResult.fromResponse(response, PushResult.class));
    }

    public void failed(Exception ex)
    {
        futureResult.failed(ex);
    }


    public void cancelled()
    {
        futureResult.cancel(true);
    }
}