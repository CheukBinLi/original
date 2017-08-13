package com.cheuks.bin.original.weixin.bak.messageModel;

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
public class VoiceMessageResponse extends BaseMessageResponse {

    private static final long serialVersionUID = 6493164237291196793L;

    public VoiceMessageResponse() {
        super("voice");
    }

    @Alias("MediaId")
    private String mediaId;// 通过素材管理中的接口上传多媒体文件，得到的id

    public String getMediaId() {
        return mediaId;
    }

    public VoiceMessageResponse setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

}
