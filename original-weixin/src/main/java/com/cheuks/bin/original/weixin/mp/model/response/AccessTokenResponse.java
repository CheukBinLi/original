package com.cheuks.bin.original.weixin.mp.model.response;

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
public class AccessTokenResponse extends MpBaseModel {

    private static final long serialVersionUID = -1437617877720962612L;

    // access_token
    @JsonProperty("access_token")
    @Alias("access_token")
    private String accessToken;

    // expires_in
    @JsonProperty("expires_in")
    @Alias("expires_in")
    private Long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public AccessTokenResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public AccessTokenResponse setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public AccessTokenResponse() {
        super();
    }

}
