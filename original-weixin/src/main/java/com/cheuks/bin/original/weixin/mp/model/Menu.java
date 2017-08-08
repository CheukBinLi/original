package com.cheuks.bin.original.weixin.mp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * 
 * @Title: original-weixin
 * @Description: 自定义菜单创建接口
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月3日 下午1:34:10
 *
 */
public class Menu extends MpBaseModel {

    private static final long serialVersionUID = 8367302464099792967L;

    @JsonProperty("button")
    private List<Button> button;

    public List<Button> getButton() {
        return button;
    }

    public Menu setButton(List<Button> button) {
        this.button = button;
        return this;
    }

}
