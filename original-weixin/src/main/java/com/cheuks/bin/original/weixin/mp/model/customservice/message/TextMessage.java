package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TextMessage extends BaseMessage {

    private static final long serialVersionUID = -14526741556805998L;

    public TextMessage setContent(String content) {
        getBody().put("content", content);
        return this;
    }

    @JsonIgnore
    public String getContent() {
        return (String) getBody().get("content");
    }

    public TextMessage() {
        super("text");
    }

    @Override
    @JsonProperty("text")
    public Map<String, Object> getBody() {
        return super.getBody();
    }

    public TextMessage(String touser, String content) {
        this();
        this.setTouser(touser);
        this.setContent(content);
    }

}
