package com.cheuks.bin.original.weixin.mp.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * 
 * @Title: original-weixin
 * @Description: 菜单按钮
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月3日 下午1:31:07
 *
 */
public class Button {

    // sub_button 否 二级菜单数组，个数应为1~5个
    @JsonProperty("sub_button")
    private List<Button> subButton;

    // type 是 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
    @JsonProperty("type")
    private String type;

    // name 是 菜单标题，不超过16个字节，子菜单不超过60个字节
    @JsonProperty("name")
    private String name;

    // key click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节
    @JsonProperty("key")
    private String key;

    // url view、miniprogram类型必须
    // 网页链接，用户点击菜单可打开链接，不超过1024字节。type为miniprogram时，不支持小程序的老版本客户端将打开本url。
    @JsonProperty("url")
    private String url;

    // media_id media_id类型和view_limited类型必须 调用新增永久素材接口返回的合法media_id
    @JsonProperty("media_id")
    private String mediaId;

    // appid miniprogram类型必须 小程序的appid（仅认证公众号可配置）
    @JsonProperty("appid")
    private String appId;

    // pagepath miniprogram类型必须 小程序的页面路径
    @JsonProperty("pagepath")
    private String pagePath;

    public List<Button> getSubButton() {
        return subButton;
    }

    public Button setSubButton(List<Button> subButton) {
        this.subButton = subButton;
        return this;
    }

    public String getType() {
        return type;
    }

    public Button setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Button setName(String name) {
        this.name = name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Button setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Button setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public Button setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public Button setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getPagePath() {
        return pagePath;
    }

    public Button setPagePath(String pagePath) {
        this.pagePath = pagePath;
        return this;
    }

}
