package com.cheuks.bin.original.weixin.mp.model.request;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeiXinUserInfoRequest extends MpBaseModel {

    private static final long serialVersionUID = -5799575434367963063L;

    // TOKEN
    @JsonProperty("access_token")
    @Alias("access_token")
    private String accessToken;

    // openid 用户的标识，对当前公众号唯一
    @JsonProperty("openid")
    @Alias("openid")
    private String openId;

    // 语言
    @JsonProperty("lang")
    @Alias("lang")
    private String lang = "zh_CN";

    public String getAccessToken() {
        return accessToken;
    }

    public WeiXinUserInfoRequest setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WeiXinUserInfoRequest setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public WeiXinUserInfoRequest setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public WeiXinUserInfoRequest(String accessToken, String openId) {
        super();
        this.accessToken = accessToken;
        this.openId = openId;
    }

    public WeiXinUserInfoRequest() {
        super();
    }

}
