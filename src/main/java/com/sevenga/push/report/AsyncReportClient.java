package com.sevenga.push.report;

import com.sevenga.push.common.TimeUnit;
import com.sevenga.push.common.connection.AsyncHttpClient;
import com.sevenga.push.common.resp.*;
import com.sevenga.push.utils.StringUtils;
import org.apache.http.concurrent.BasicFuture;

import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by lizi on 15/9/14.
 */
public class AsyncReportClient {
    private static final String HOST_NAME = "report.jpush.cn";
    private static final String REPORT_RECEIVE_PATH = "/v3/received";
    private static final String REPORT_USER_PATH = "/v3/users";
    private static final String REPORT_MESSAGE_PATH = "/v3/messages";
    private static final Pattern MSGID_PATTERNS = Pattern.compile("[^0-9, ]");
    private AsyncHttpClient httpAsyncClient;

    public AsyncReportClient(AsyncHttpClient httpAsyncClient) {
        this.httpAsyncClient = httpAsyncClient;
    }

    public BasicFuture<ReceivedsResult>  getReceiveds(String[] msgIdArray) {
        return this.getReceiveds(StringUtils.arrayToString(msgIdArray));
    }

    public BasicFuture<ReceivedsResult> getReceiveds(String msgIds){
        checkMsgids(msgIds);
        String uri = REPORT_RECEIVE_PATH + "?msg_ids=" + msgIds;

        BasicFuture<ReceivedsResult> futureResult = new BasicFuture<ReceivedsResult>(null);
        httpAsyncClient.sendGet(HOST_NAME, uri, new ReportRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<MessagesResult> getMessages(String msgIds) throws APIConnectionException, APIRequestException {
        checkMsgids(msgIds);
        String uri = REPORT_MESSAGE_PATH + "?msg_ids=" + msgIds;
        BasicFuture<MessagesResult> futureResult = new BasicFuture<MessagesResult>(null);
        httpAsyncClient.sendGet(HOST_NAME, uri, new MessageRequestCallback(futureResult));
        return futureResult;
    }

    public BasicFuture<UsersResult> getUsers(TimeUnit timeUnit, String start, int duration){
        String startEncoded = null;

        try {
            startEncoded = URLEncoder.encode(start, "utf-8");
        } catch (Exception var7) {
            ;
        }

        String uri = REPORT_USER_PATH + "?time_unit=" + timeUnit.toString() + "&start=" + startEncoded + "&duration=" + duration;
        BasicFuture<UsersResult> futureResult = new BasicFuture<UsersResult>(null);

        httpAsyncClient.sendGet(HOST_NAME, uri, new BaseRequestCallback<UsersResult>(futureResult, UsersResult.class));
        return futureResult;
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
                int len = splits.length;

                for(int i = 0; i < len; ++i) {
                    String s = e[i];
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
