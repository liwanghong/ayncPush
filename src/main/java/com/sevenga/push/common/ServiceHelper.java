package com.sevenga.push.common;

/**
 * Created by lizi on 15/9/7.
 */
import com.sevenga.push.utils.Base64;
import com.sevenga.push.utils.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class ServiceHelper {
    private static final Pattern PUSH_PATTERNS = Pattern.compile("[^a-zA-Z0-9]");
    private static final String BASIC_PREFIX = "Basic";
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final int MIN = 100000;
    private static final int MAX = 2147483647;
    private static final int MAX_BADGE_NUMBER = 99999;

    public ServiceHelper() {
    }

    public static boolean isValidIntBadge(int intBadge) {
        return intBadge >= 0 && intBadge <= 99999;
    }

    public static int generateSendno() {
        return RANDOM.nextInt(2147383648) + 100000;
    }

    public static String getBasicAuthorization(String username, String password) {
        String encodeKey = username + ":" + password;
        return "Basic " + String.valueOf(Base64.encode(encodeKey.getBytes()));
    }

    public static void checkBasic(String appKey, String masterSecret) {
        if(!StringUtils.isEmpty(appKey) && !StringUtils.isEmpty(masterSecret)) {
            if(appKey.length() != 24 || masterSecret.length() != 24 || PUSH_PATTERNS.matcher(appKey).find() || PUSH_PATTERNS.matcher(masterSecret).find()) {
                throw new IllegalArgumentException("appKey and masterSecret format is incorrect. They should be 24 size, and be composed with alphabet and numbers. Please confirm that they are coming from JPush Web Portal.");
            }
        } else {
            throw new IllegalArgumentException("appKey and masterSecret are both required.");
        }
    }

    public static JsonArray fromSet(Set<String> sets) {
        JsonArray array = new JsonArray();
        if(null != sets && sets.size() > 0) {
            Iterator i$ = sets.iterator();

            while(i$.hasNext()) {
                String item = (String)i$.next();
                array.add(new JsonPrimitive(item));
            }
        }

        return array;
    }
}

