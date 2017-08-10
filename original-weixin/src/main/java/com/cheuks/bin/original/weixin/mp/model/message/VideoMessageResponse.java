package com.cheuks.bin.original.weixin.mp.model.message;

import java.io.Serializable;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复视频消息
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午2:18:43
 *
 */
public class VideoMessageResponse implements Serializable {

    private static final long serialVersionUID = -1148254018960644140L;

    @Alias("ToUserName")
    private String toUserName;// 接收方帐号（收到的OpenID）

    @Alias("FromUserName")
    private String fromUserName;// 开发者微信号

    @Alias("CreateTime")
    private Long createTime = System.currentTimeMillis();// 消息创建时间 （整型）

    @Alias("MsgType")
    private String msgType = "video";// video

    @Alias("MediaId")
    private String mediaId;// 通过素材管理中的接口上传多媒体文件，得到的id

    @Alias("Title")
    private String title;// 视频消息的标题

    @Alias("Description")
    private String description;// 视频消息的描述

    public String getToUserName() {
        return toUserName;
    }

    public VideoMessageResponse setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public VideoMessageResponse setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public VideoMessageResponse setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMsgType() {
        return msgType;
    }

    public VideoMessageResponse setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public VideoMessageResponse setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public VideoMessageResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public VideoMessageResponse setDescription(String description) {
        this.description = description;
        return this;
    }

}
