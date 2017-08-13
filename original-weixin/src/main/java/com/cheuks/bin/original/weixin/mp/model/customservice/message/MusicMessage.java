package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MusicMessage extends BaseMessage {

    private static final long serialVersionUID = -14526741556805998L;

    public MusicMessage setTitle(String title) {
        getBody().put("title", title);
        return this;
    }

    @JsonIgnore
    public String getTitle() {
        return (String) getBody().get("title");
    }

    public MusicMessage setDescription(String description) {
        getBody().put("description", description);
        return this;
    }

    @JsonIgnore
    public String getDescription() {
        return (String) getBody().get("description");
    }

    public MusicMessage setMusicUrl(String musicUrl) {
        getBody().put("musicurl", musicUrl);
        return this;
    }

    @JsonIgnore
    public String getHqMusicrl() {
        return (String) getBody().get("hqMusicrl");
    }

    public MusicMessage setHqMusicrl(String hqMusicrl) {
        getBody().put("hqmusicurl", hqMusicrl);
        return this;
    }

    @JsonIgnore
    public String getMusicUrl() {
        return (String) getBody().get("musicurl");
    }

    public MusicMessage setThumbMediaId(String thumbMediaId) {
        getBody().put("thumb_media_id", thumbMediaId);
        return this;
    }

    @JsonIgnore
    public String getThumbMediaId() {
        return (String) getBody().get("thumb_media_id");
    }

    public MusicMessage() {
        super("music");
    }

    @Override
    @JsonProperty("music")
    public Map<String, Object> getBody() {
        return super.getBody();
    }

}
