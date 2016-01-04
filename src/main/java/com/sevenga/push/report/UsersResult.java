package com.sevenga.push.report;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.common.TimeUnit;
import com.sevenga.push.common.resp.BaseResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class UsersResult extends BaseResult {
    @Expose
    public TimeUnit time_unit;
    @Expose
    public String start;
    @Expose
    public int duration;
    @Expose
    public List<UsersResult.User> items = new ArrayList();

    public UsersResult() {
    }

    public static class Ios {
        @SerializedName("new")
        @Expose
        public long add;
        @Expose
        public int online;
        @Expose
        public int active;

        public Ios() {
        }
    }

    public static class Android {
        @SerializedName("new")
        @Expose
        public long add;
        @Expose
        public int online;
        @Expose
        public int active;

        public Android() {
        }
    }

    public static class User {
        @Expose
        public String time;
        @Expose
        public UsersResult.Android android;
        @Expose
        public UsersResult.Ios ios;

        public User() {
        }
    }
}
