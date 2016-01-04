package com.sevenga.push.device;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.common.resp.BaseResult;
import com.google.gson.annotations.Expose;
import java.util.List;

public class TagAliasResult extends BaseResult {
    @Expose
    public List<String> tags;
    @Expose
    public String alias;

    public TagAliasResult() {
    }
}
