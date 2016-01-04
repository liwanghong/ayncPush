package com.sevenga.push.device;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.common.resp.BaseResult;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class TagListResult extends BaseResult {
    @Expose
    public List<String> tags = new ArrayList();

    public TagListResult() {
    }
}
