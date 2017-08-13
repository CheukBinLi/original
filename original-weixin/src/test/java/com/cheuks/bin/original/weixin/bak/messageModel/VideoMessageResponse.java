package com.cheuks.bin.original.weixin.bak.messageModel;

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
public class VideoMessageResponse extends BaseMessageResponse {

    private static final long serialVersionUID = -1148254018960644140L;

    public VideoMessageResponse() {
        super("video");
    }

    @Alias("MediaId")
    private String mediaId;// 通过素材管理中的接口上传多媒体文件，得到的id

    @Alias("Title")
    private String title;// 视频消息的标题

    @Alias("Description")
    private String description;// 视频消息的描述

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
