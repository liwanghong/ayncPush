package com.sevenga.push.common.resp;

import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by lizi on 15/9/15.
 */
public class DefaultRequestCallback implements FutureCallback<ResponseWrapper> {
    private BasicFuture<DefaultResult> futureResult;

    public DefaultRequestCallback(BasicFuture<DefaultResult> futureResult) {
        this.futureResult = futureResult;
    }

    public void completed(ResponseWrapper response)
    {
        futureResult.completed(DefaultResult.fromResponse(response));
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
