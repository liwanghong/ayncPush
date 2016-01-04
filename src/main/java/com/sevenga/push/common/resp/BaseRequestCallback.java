package com.sevenga.push.common.resp;

import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by lizi on 15/9/14.
 */
public class BaseRequestCallback<T extends BaseResult> implements FutureCallback<ResponseWrapper> {
    private BasicFuture<T> futureResult;
    private Class<T> clazz;

    public BaseRequestCallback(BasicFuture<T> futureResult, Class<T> clazz) {
        this.futureResult = futureResult;
        this.clazz = clazz;
    }

    public void completed(ResponseWrapper response)
    {
        futureResult.completed(BaseResult.fromResponse(response, clazz));
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
