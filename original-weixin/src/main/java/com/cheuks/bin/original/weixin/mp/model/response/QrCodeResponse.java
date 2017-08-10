package com.cheuks.bin.original.weixin.mp.model.response;

import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
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
public class QrCodeResponse extends MpBaseModel {

    private static final long serialVersionUID = -2041387783354111097L;

    // 返回
    // ticket 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
    @JsonProperty("ticket")
    private String ticket;

    @JsonProperty("expire_seconds")
    private Long expireSeconds;

    // url 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    @JsonProperty("url")
    private String url;

    public String getTicket() {
        return ticket;
    }

    public QrCodeResponse setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public Long getExpireSeconds() {
        return expireSeconds;
    }

    public QrCodeResponse setExpireSeconds(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public QrCodeResponse setUrl(String url) {
        this.url = url;
        return this;
    }

}
