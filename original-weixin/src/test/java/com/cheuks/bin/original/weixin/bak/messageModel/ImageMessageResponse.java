package com.cheuks.bin.original.weixin.bak.messageModel;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复图片消息
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午2:12:31
 *
 */
public class ImageMessageResponse extends BaseMessageResponse {

    private static final long serialVersionUID = -2044366865205883414L;

    @Alias("MediaId")
    private String mediaId;// 通过素材管理中的接口上传多媒体文件，得到的id。

    public String getMediaId() {
        return mediaId;
    }

    public ImageMessageResponse setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public ImageMessageResponse() {
        super("image");
    }

}
