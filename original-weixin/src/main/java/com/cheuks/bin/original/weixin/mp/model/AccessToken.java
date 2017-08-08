package com.cheuks.bin.original.weixin.mp.model;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * 
 * @Title: original-weixin
 * @Description: accessToken 接口
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月3日 下午1:34:25
 *
 */
public class AccessToken extends MpBaseModel {

    private static final long serialVersionUID = -1437617877720962612L;

    // grant_type 是 获取access_token填写client_credential
    @JsonProperty("grant_type")
    @Alias("grant_type")
    private String grantType = "client_credential";

    // appid 是 第三方用户唯一凭证
    @JsonProperty("appid")
    @Alias("appid")
    private String appId;

    // secret 是 第三方用户唯一凭证密钥，即appsecret
    @JsonProperty("secret")
    @Alias("secret")
    private String secret;

    // access_token
    @JsonProperty("access_token")
    @Alias("access_token")
    private String accessToken;

    // expires_in
    @JsonProperty("expires_in")
    @Alias("expires_in")
    private Long expiresIn;

    public String getGrantType() {
        return grantType;
    }

    public AccessToken setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public AccessToken setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public AccessToken setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AccessToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public AccessToken setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public AccessToken() {
        super();
    }

    public AccessToken(String appId, String secret, String grantType) {
        super();
        this.grantType = grantType;
        this.appId = appId;
        this.secret = secret;
    }

}
