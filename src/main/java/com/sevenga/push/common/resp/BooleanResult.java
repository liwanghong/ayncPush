package com.sevenga.push.common.resp;

/**
 * Created by lizi on 15/9/7.
 */
import com.google.gson.annotations.Expose;

public class BooleanResult extends DefaultResult {
    @Expose
    public boolean result;

    public BooleanResult() {
    }
}
