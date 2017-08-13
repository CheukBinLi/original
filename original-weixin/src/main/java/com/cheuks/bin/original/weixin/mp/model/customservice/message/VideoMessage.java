package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoMessage extends BaseMessage {

    private static final long serialVersionUID = -14526741556805998L;

    public VideoMessage setMediaId(String mediaId) {
        getBody().put("media_id", mediaId);
        return this;
    }

    @JsonIgnore
    public String getMediaId() {
        return (String) getBody().get("media_id");
    }

    public VideoMessage setThumbMediaId(String thumbMediaId) {
        getBody().put("thumb_media_id", thumbMediaId);
        return this;
    }

    @JsonIgnore
    public String getThumbMediaId() {
        return (String) getBody().get("thumb_media_id");
    }

    public VideoMessage setTitle(String title) {
        getBody().put("title", title);
        return this;
    }

    @JsonIgnore
    public String getTitle() {
        return (String) getBody().get("title");
    }

    public VideoMessage setDescription(String description) {
        getBody().put("description", description);
        return this;
    }

    @JsonIgnore
    public String getDescription() {
        return (String) getBody().get("description");
    }

    public VideoMessage() {
        super("video");
    }

    @Override
    @JsonProperty("video")
    public Map<String, Object> getBody() {
        return super.getBody();
    }

}
