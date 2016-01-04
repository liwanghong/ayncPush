package com.sevenga.push.report;

import com.sevenga.push.common.resp.ResponseWrapper;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by lizi on 15/9/14.
 */
public class ReportRequestCallback implements FutureCallback<ResponseWrapper> {
    private BasicFuture<ReceivedsResult> futureResult;

    public ReportRequestCallback(BasicFuture<ReceivedsResult> futureResult) {
        this.futureResult = futureResult;
    }

    public void completed(ResponseWrapper response)
    {
        futureResult.completed(ReceivedsResult.fromResponse(response));
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