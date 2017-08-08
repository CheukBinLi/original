package com.cheuks.bin.original.weixin.mp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * 
 * @Title: original-weixin
 * @Description: 二维码
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月3日 下午10:16:44
 *
 */
public class QrCode extends MpBaseModel {

    private static final long serialVersionUID = -2041387783354111097L;

    // expire_seconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
    @JsonProperty("expire_seconds")
    private String expireSeconds;

    // action_name 二维码类型，
    // QR_SCENE为临时的整型参数值，
    // QR_STR_SCENE为临时的字符串参数值，
    // QR_LIMIT_SCENE为永久的整型参数值，
    // QR_LIMIT_STR_SCENE为永久的字符串参数值
    @JsonProperty("action_name")
    private String actionName;

    // action_info 二维码详细信息
    @JsonProperty("action_info")
    private String actionInfo;

    // scene_id 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
    @JsonProperty("scene_id")
    private String sceneId;

    // scene_str 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
    @JsonProperty("scene_str")
    private String sceneStr;

    // 返回
    // ticket 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
    @JsonProperty("ticket")
    private String ticket;

    // url 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    @JsonProperty("url")
    private String url;

    public String getExpireSeconds() {
        return expireSeconds;
    }

    public QrCode setExpireSeconds(String expireSeconds) {
        this.expireSeconds = expireSeconds;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public QrCode setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public String getActionInfo() {
        return actionInfo;
    }

    public QrCode setActionInfo(String actionInfo) {
        this.actionInfo = actionInfo;
        return this;
    }

    public String getSceneId() {
        return sceneId;
    }

    public QrCode setSceneId(String sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public QrCode setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
        return this;
    }

    public String getTicket() {
        return ticket;
    }

    public QrCode setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public QrCode setUrl(String url) {
        this.url = url;
        return this;
    }
    
    
}
