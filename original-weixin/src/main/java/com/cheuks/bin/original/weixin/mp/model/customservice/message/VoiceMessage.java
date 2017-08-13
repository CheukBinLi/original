package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VoiceMessage extends BaseMessage {

    private static final long serialVersionUID = -14526741556805998L;

    public VoiceMessage setMediaId(String mediaId) {
        getBody().put("media_id", mediaId);
        return this;
    }

    @JsonIgnore
    public String getContent() {
        return (String) getBody().get("media_id");
    }

    public VoiceMessage() {
        super("voice");
    }

    @Override
    @JsonProperty("voice")
    public Map<String, Object> getBody() {
        return super.getBody();
    }

}
