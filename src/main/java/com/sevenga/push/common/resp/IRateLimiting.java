package com.sevenga.push.common.resp;

/**
 * Created by lizi on 15/9/7.
 */
public interface IRateLimiting {
    int getRateLimitQuota();

    int getRateLimitRemaining();

    int getRateLimitReset();
}
