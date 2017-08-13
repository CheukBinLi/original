package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MpNewsMessage extends BaseMessage {

    private static final long serialVersionUID = -14526741556805998L;

    public MpNewsMessage setMediaId(String mediaId) {
        getBody().put("media_id", mediaId);
        return this;
    }

    @JsonIgnore
    public String getMediaId() {
        return (String) getBody().get("media_id");
    }

    public MpNewsMessage() {
        super("mpnews");
    }

    @Override
    @JsonProperty("mpnews")
    public Map<String, Object> getBody() {
        return super.getBody();
    }

}
