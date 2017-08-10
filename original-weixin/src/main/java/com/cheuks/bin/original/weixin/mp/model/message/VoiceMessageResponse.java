package com.cheuks.bin.original.weixin.mp.model.message;

import java.io.Serializable;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复语音消息
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午2:16:24
 *
 */
public class VoiceMessageResponse implements Serializable {

    private static final long serialVersionUID = 6493164237291196793L;

    @Alias("ToUserName")
    private String toUserName;// 接收方帐号（收到的OpenID）

    @Alias("FromUserName")
    private String fromUserName;// 开发者微信号

    @Alias("CreateTime")
    private Long createTime = System.currentTimeMillis();// 消息创建时间戳 （整型）

    @Alias("MsgType")
    private String msgType = "voice";// 语音，voice

    @Alias("MediaId")
    private String mediaId;// 通过素材管理中的接口上传多媒体文件，得到的id

    public String getToUserName() {
        return toUserName;
    }

    public VoiceMessageResponse setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public VoiceMessageResponse setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public VoiceMessageResponse setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMsgType() {
        return msgType;
    }

    public VoiceMessageResponse setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public VoiceMessageResponse setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

}
