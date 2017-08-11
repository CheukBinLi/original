package com.cheuks.bin.original.weixin.mp.model.request;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

/***
 * 
 * @Title: original-weixin
 * @Description: 永久素材
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月11日 下午2:30:16
 *
 */
public class CreateMaterialRequest extends MpBaseModel {

    private static final long serialVersionUID = -8352206502534213493L;

    private String title;// 标题

    @Alias("thumb_media_id")
    private String thumbMediaId;// 图文消息的封面图片素材id（必须是永久mediaID）

    private String author;// 作者

    private String digest;// 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前64个字。

    @Alias("showCoverPic")
    private int showCoverPic;// 是否显示封面，0为false，即不显示，1为true，即显示

    private String content;// 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源"上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。

    @Alias("contentSourceUrl")
    private String contentSourceUrl;// 图文消息的原文地址，即点击“阅读原文”后的URL

    public String getTitle() {
        return title;
    }

    public CreateMaterialRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public CreateMaterialRequest setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CreateMaterialRequest setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getDigest() {
        return digest;
    }

    public CreateMaterialRequest setDigest(String digest) {
        this.digest = digest;
        return this;
    }

    public int getShowCoverPic() {
        return showCoverPic;
    }

    public CreateMaterialRequest setShowCoverPic(int showCoverPic) {
        this.showCoverPic = showCoverPic;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CreateMaterialRequest setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public CreateMaterialRequest setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
        return this;
    }

}
