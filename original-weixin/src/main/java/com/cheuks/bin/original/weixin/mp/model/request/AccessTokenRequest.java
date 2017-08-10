package com.cheuks.bin.original.weixin.mp.model.request;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
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
public class AccessTokenRequest extends MpBaseModel {

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

    public String getGrantType() {
        return grantType;
    }

    public AccessTokenRequest setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public AccessTokenRequest setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public AccessTokenRequest setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public AccessTokenRequest() {
        super();
    }

    public AccessTokenRequest(String appId, String secret) {
        super();
        this.appId = appId;
        this.secret = secret;
    }

}
