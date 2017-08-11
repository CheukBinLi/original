package com.cheuks.bin.original.weixin.mp.model.message;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复音乐消息
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午2:20:05
 *
 */
public class MusicMessageResponse extends BaseMessageResponse {

    private static final long serialVersionUID = -1162013691837281695L;

    @Alias("Title")
    private String title;// 音乐标题

    @Alias("Description")
    private String description;// 音乐描述

    @Alias("MusicURL")
    private String musicURL;// 音乐链接

    @Alias("HQMusicUrl")
    private String hqMusicUrl;// 高质量音乐链接，WIFI环境优先使用该链接播放音乐

    @Alias("ThumbMediaId")
    private String thumbMediaId;// 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id


    public MusicMessageResponse() {
        super("music");
    }

    public String getTitle() {
        return title;
    }

    public MusicMessageResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MusicMessageResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public MusicMessageResponse setMusicURL(String musicURL) {
        this.musicURL = musicURL;
        return this;
    }

    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    public MusicMessageResponse setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
        return this;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public MusicMessageResponse setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
        return this;
    }

}
