package com.cheuks.bin.original.weixin.mp.model.message;

import java.io.Serializable;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复文本消息
 * @Company: 
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日  下午2:12:54
 *
 */
public class TextMessageResponse implements Serializable {

    private static final long serialVersionUID = 1277430317241352680L;

    @Alias("ToUserName")
    private String toUserName;// 接收方帐号（收到的OpenID）

    @Alias("FromUserName")
    private String fromUserName;// 开发者微信号

    @Alias("CreateTime")
    private Long createTime = System.currentTimeMillis();// 消息创建时间 （整型）

    @Alias("MsgType")
    private String msgType = "text";// text

    @Alias("Content")
    private String content;// 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）

    public String getToUserName() {
        return toUserName;
    }

    public TextMessageResponse setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public TextMessageResponse setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public TextMessageResponse setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMsgType() {
        return msgType;
    }

    public TextMessageResponse setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public TextMessageResponse setContent(String content) {
        this.content = content;
        return this;
    }

}
