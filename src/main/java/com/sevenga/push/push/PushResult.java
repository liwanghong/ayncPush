package com.sevenga.push.push;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.common.resp.BaseResult;
import com.google.gson.annotations.Expose;

public class PushResult extends BaseResult {
    @Expose
    public long msg_id;
    @Expose
    public int sendno;

    public PushResult() {
    }
}
