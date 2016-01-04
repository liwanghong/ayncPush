package com.sevenga.push.report;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.common.ServiceHelper;
import com.sevenga.push.common.TimeUnit;
import com.sevenga.push.common.connection.HttpProxy;
import com.sevenga.push.common.connection.NativeHttpClient;
import com.sevenga.push.common.resp.APIConnectionException;
import com.sevenga.push.common.resp.APIRequestException;
import com.sevenga.push.common.resp.BaseResult;
import com.sevenga.push.common.resp.ResponseWrapper;
import com.sevenga.push.utils.StringUtils;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class ReportClient {
    private static final String REPORT_HOST_NAME = "https://report.jpush.cn";
    private static final String REPORT_RECEIVE_PATH = "/v3/received";
    private static final String REPORT_USER_PATH = "/v3/users";
    private static final String REPORT_MESSAGE_PATH = "/v3/messages";
    private final NativeHttpClient _httpClient;
    private static final Pattern MSGID_PATTERNS = Pattern.compile("[^0-9, ]");

    public ReportClient(String masterSecret, String appKey) {
        this(masterSecret, appKey, 3, (HttpProxy)null);
    }

    public ReportClient(String masterSecret, String appKey, int maxRetryTimes) {
        this(masterSecret, appKey, maxRetryTimes, (HttpProxy)null);
    }

    public ReportClient(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy) {
        ServiceHelper.checkBasic(appKey, masterSecret);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        this._httpClient = new NativeHttpClient(authCode, maxRetryTimes, proxy);
    }

    public ReceivedsResult getReceiveds(String[] msgIdArray) throws APIConnectionException, APIRequestException {
        return this.getReceiveds(StringUtils.arrayToString(msgIdArray));
    }

    public ReceivedsResult getReceiveds(String msgIds) throws APIConnectionException, APIRequestException {
        checkMsgids(msgIds);
        String url = "https://report.jpush.cn/v3/received?msg_ids=" + msgIds;
        ResponseWrapper response = this._httpClient.sendGet(url);
        return ReceivedsResult.fromResponse(response);
    }

    public MessagesResult getMessages(String msgIds) throws APIConnectionException, APIRequestException {
        checkMsgids(msgIds);
        String url = "https://report.jpush.cn/v3/messages?msg_ids=" + msgIds;
        ResponseWrapper response = this._httpClient.sendGet(url);
        return MessagesResult.fromResponse(response);
    }

    public UsersResult getUsers(TimeUnit timeUnit, String start, int duration) throws APIConnectionException, APIRequestException {
        String startEncoded = null;

        try {
            startEncoded = URLEncoder.encode(start, "utf-8");
        } catch (Exception var7) {
            ;
        }

        String url = "https://report.jpush.cn/v3/users?time_unit=" + timeUnit.toString() + "&start=" + startEncoded + "&duration=" + duration;
        ResponseWrapper response = this._httpClient.sendGet(url);
        return (UsersResult)BaseResult.fromResponse(response, UsersResult.class);
    }

    public static void checkMsgids(String msgIds) {
        if(StringUtils.isTrimedEmpty(msgIds)) {
            throw new IllegalArgumentException("msgIds param is required.");
        } else if(MSGID_PATTERNS.matcher(msgIds).find()) {
            throw new IllegalArgumentException("msgIds param format is incorrect. It should be msg_id (number) which response from JPush Push API. If there are many, use \',\' as interval. ");
        } else {
            msgIds = msgIds.trim();
            if(msgIds.endsWith(",")) {
                msgIds = msgIds.substring(0, msgIds.length() - 1);
            }

            String[] splits = msgIds.split(",");

            try {
                String[] e = splits;
                int len$ = splits.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String s = e[i$];
                    s = s.trim();
                    if(!StringUtils.isEmpty(s)) {
                        Integer.parseInt(s);
                    }
                }

            } catch (NumberFormatException var6) {
                throw new IllegalArgumentException("Every msg_id should be valid Integer number which splits by \',\'");
            }
        }
    }
}
