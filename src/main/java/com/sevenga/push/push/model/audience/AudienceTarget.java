package com.sevenga.push.push.model.audience;

/**
 * Created by lizi on 15/9/8.
 */
import com.sevenga.push.push.model.PushModel;
import com.sevenga.push.utils.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AudienceTarget implements PushModel {
    private final AudienceType audienceType;
    private final Set<String> values;

    private AudienceTarget(AudienceType audienceType, Set<String> values) {
        this.audienceType = audienceType;
        this.values = values;
    }

    public static AudienceTarget.Builder newBuilder() {
        return new AudienceTarget.Builder();
    }

    public static AudienceTarget tag(String... tag) {
        return newBuilder().setAudienceType(AudienceType.TAG).addAudienceTargetValues(tag).build();
    }

    public static AudienceTarget tag(Collection<String> tags) {
        return newBuilder().setAudienceType(AudienceType.TAG).addAudienceTargetValues(tags).build();
    }

    public static AudienceTarget tag_and(String... tag) {
        return newBuilder().setAudienceType(AudienceType.TAG_AND).addAudienceTargetValues(tag).build();
    }

    public static AudienceTarget tag_and(Collection<String> tags) {
        return newBuilder().setAudienceType(AudienceType.TAG_AND).addAudienceTargetValues(tags).build();
    }

    public static AudienceTarget alias(String... alias) {
        return newBuilder().setAudienceType(AudienceType.ALIAS).addAudienceTargetValues(alias).build();
    }

    public static AudienceTarget alias(Collection<String> aliases) {
        return newBuilder().setAudienceType(AudienceType.ALIAS).addAudienceTargetValues(aliases).build();
    }

    public static AudienceTarget registrationId(String... registrationId) {
        return newBuilder().setAudienceType(AudienceType.REGISTRATION_ID).addAudienceTargetValues(registrationId).build();
    }

    public static AudienceTarget registrationId(Collection<String> registrationIds) {
        return newBuilder().setAudienceType(AudienceType.REGISTRATION_ID).addAudienceTargetValues(registrationIds).build();
    }

    public AudienceType getAudienceType() {
        return this.audienceType;
    }

    public String getAudienceTypeValue() {
        return this.audienceType.value();
    }

    public JsonElement toJSON() {
        JsonArray array = new JsonArray();
        if(null != this.values) {
            Iterator i$ = this.values.iterator();

            while(i$.hasNext()) {
                String value = (String)i$.next();
                array.add(new JsonPrimitive(value));
            }
        }

        return array;
    }

    public static class Builder {
        private AudienceType audienceType = null;
        private Set<String> valueBuilder = null;

        public Builder() {
        }

        public AudienceTarget.Builder setAudienceType(AudienceType audienceType) {
            this.audienceType = audienceType;
            return this;
        }

        public AudienceTarget.Builder addAudienceTargetValue(String value) {
            if(null == this.valueBuilder) {
                this.valueBuilder = new HashSet();
            }

            this.valueBuilder.add(value);
            return this;
        }

        public AudienceTarget.Builder addAudienceTargetValues(Collection<String> values) {
            if(null == this.valueBuilder) {
                this.valueBuilder = new HashSet();
            }

            Iterator i$ = values.iterator();

            while(i$.hasNext()) {
                String value = (String)i$.next();
                this.valueBuilder.add(value);
            }

            return this;
        }

        public AudienceTarget.Builder addAudienceTargetValues(String... values) {
            if(null == this.valueBuilder) {
                this.valueBuilder = new HashSet();
            }

            String[] arr$ = values;
            int len$ = values.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String value = arr$[i$];
                this.valueBuilder.add(value);
            }

            return this;
        }

        public AudienceTarget build() {
            Preconditions.checkArgument(null != this.audienceType, "AudienceType should be set.");
            Preconditions.checkArgument(null != this.valueBuilder, "Target values should be set one at least.");
            return new AudienceTarget(this.audienceType, this.valueBuilder);
        }
    }
}
