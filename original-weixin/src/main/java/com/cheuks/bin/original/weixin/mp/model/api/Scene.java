package com.cheuks.bin.original.weixin.mp.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * 
 * @Title: original-weixin
 * @Description: 二维码参数
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午1:41:54
 *
 */
public class Scene {

    // scene_id 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
    @JsonProperty("scene_id")
    private Integer sceneId;

    // scene_str 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
    @JsonProperty("scene_str")
    private String sceneStr;

    public Integer getSceneId() {
        return sceneId;
    }

    public Scene setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public Scene setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
        return this;
    }
}
