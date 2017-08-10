package com.cheuks.bin.original.weixin.mp.model.message;

import java.io.Serializable;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复图片消息
 * @Company: 
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日  下午2:12:31
 *
 */
public class ImageMessageResponse implements Serializable {

    private static final long serialVersionUID = -2044366865205883414L;

    @Alias("ToUserName")
    private String toUserName;// 接收方帐号（收到的OpenID）

    @Alias("FromUserName")
    private String fromUserName;// 开发者微信号

    @Alias("CreateTime")
    private Long createTime = System.currentTimeMillis();// 消息创建时间 （整型）

    @Alias("MsgType")
    private String MsgType = "image";// image

    @Alias("MediaId")
    private String mediaId;// 通过素材管理中的接口上传多媒体文件，得到的id。

    public String getToUserName() {
        return toUserName;
    }

    public ImageMessageResponse setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public ImageMessageResponse setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ImageMessageResponse setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMsgType() {
        return MsgType;
    }

    public ImageMessageResponse setMsgType(String msgType) {
        MsgType = msgType;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public ImageMessageResponse setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

}
