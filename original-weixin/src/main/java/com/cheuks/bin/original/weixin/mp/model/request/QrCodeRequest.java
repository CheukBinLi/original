package com.cheuks.bin.original.weixin.mp.model.request;

import java.util.HashMap;
import java.util.Map;

import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
import com.cheuks.bin.original.weixin.mp.model.Scene;
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
public class QrCodeRequest extends MpBaseModel {

    private static final long serialVersionUID = -2041387783354111097L;

    // expire_seconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
    @JsonProperty("expire_seconds")
    private Long expireSeconds = 2592000L;

    // action_name 二维码类型，
    // QR_SCENE为临时的整型参数值，
    // QR_STR_SCENE为临时的字符串参数值，
    // QR_LIMIT_SCENE为永久的整型参数值，
    // QR_LIMIT_STR_SCENE为永久的字符串参数值
    @JsonProperty("action_name")
    private String actionName;

    // action_info 二维码详细信息
    @JsonProperty("action_info")
    // private Map<String, Scene> actionInfo = new HashMap<String, Scene>();
    private Map<String, Scene> actionInfo = new HashMap<String, Scene>();

    public Long getExpireSeconds() {
        return expireSeconds;
    }

    public QrCodeRequest setExpireSeconds(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public QrCodeRequest setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public Scene getActionInfo() {
        return this.actionInfo.get("Scene");
    }

    public QrCodeRequest setActionInfo(Scene scene) {
        this.actionInfo.put("Scene", scene);
        return this;
    }

}
