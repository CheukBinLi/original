package com.cheuks.bin.original.weixin.mp.model.api.request;

import java.net.URLEncoder;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

/***
 * 网页授权
 * 
 * @Title: original-weixin
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月11日 下午3:04:07
 *
 */
public class WebAuthorizeRequest extends MpBaseModel {

    private static final long serialVersionUID = -8605821579484394917L;

    private String appid;// 公众号的唯一标识

    @Alias("redirect_uri")
    private String redirectUri;// 授权后重定向的回调链接地址，请使用urlEncode对链接进行处理

    @Alias("response_type")
    private String responsetype = "code";// 返回类型，请填写code

    private String scope = "snsapi_userinfo";// 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）

    private String state = "STATE";// 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节

    public String getAppid() {
        return appid;
    }

    public WebAuthorizeRequest setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public WebAuthorizeRequest setRedirectUri(String redirectUri) throws Throwable {
        this.redirectUri = URLEncoder.encode(redirectUri, "utf-8");
        return this;
    }

    public String getResponsetype() {
        return responsetype;
    }

    public WebAuthorizeRequest setResponsetype(String responsetype) {
        this.responsetype = responsetype;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public WebAuthorizeRequest setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getState() {
        return state;
    }

    public WebAuthorizeRequest setState(String state) {
        this.state = state;
        return this;
    }

    //    private String  #wechat_redirect    ;// 无论直接打开还是做页面302重定向时候，必须带此参数

}
