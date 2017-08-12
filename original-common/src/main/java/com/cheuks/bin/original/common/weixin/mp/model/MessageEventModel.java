package com.cheuks.bin.original.common.weixin.mp.model;

import java.io.Serializable;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 事件推送接收模型
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午1:43:35
 *
 */
public class MessageEventModel implements Serializable {

    private static final long serialVersionUID = -7883736029643224425L;

    @Alias("ToUserName")
    private String toUserName;// 开发者微信号

    @Alias("FromUserName")
    private String fromUserName;// 发送方帐号（一个OpenID）

    @Alias("CreateTime")
    private Long createTime;// 消息创建时间 （整型）

    @Alias("MsgType")
    private String msgType;// text

    @Alias("Content")
    private String content;// 文本消息内容

    @Alias("MsgId")
    private Long msgId;// 消息id，64位整型

    @Alias("PicUrl")
    private String picUrl;// 图片链接（由系统生成）

    @Alias("MediaID")
    private String mediaID;// 语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体

    @Alias("Format")
    private String format;// 语音格式：amr

    @Alias("Recognition")
    private String recognition;// 语音识别结果，UTF8编码

    @Alias("MsgID")
    private Long m;// 消息id，64位整型

    @Alias("MediaId")
    private String mediaId;// 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。

    @Alias("ThumbMediaId")
    private String thumbMediaId;// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。

    @Alias("Location_X")
    private String locationX;// 地理位置维度

    @Alias("Location_Y")
    private String locationY;// 地理位置经度

    @Alias("Scale")
    private String scale;// 地图缩放大小

    @Alias("Label")
    private String label;// 地理位置信息

    @Alias("Title")
    private String title;// 消息标题

    @Alias("Description")
    private String description;// 消息描述

    @Alias("Url")
    private String url;// 消息链接

    @Alias("Ticket")
    private String ticket;// 二维码的ticket，可用来换取二维码图片

    @Alias("Latitude")
    private String latitude;// 地理位置纬度

    @Alias("Longitude")
    private String longitude;// 地理位置经度

    @Alias("Precision")
    private String precision;// 地理位置精度

    @Alias("Event")
    private String event;// 事件类型，VIEW

    @Alias("EventKey")
    private String eventKey;// 事件KEY值，设置的跳转URL

    @Alias("MusicURL")
    private String musicURL;// 音乐链接

    @Alias("HQMusicUrl")
    private String hqMusicUrl;// 高质量音乐链接，WIFI环境优先使用该链接播放音乐

    @Alias("ArticleCount")
    private Integer articleCount;// 图文消息个数，限制为8条以内

    @Alias("Articles")
    private String articles;// 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应

    public String getToUserName() {
        return toUserName;
    }

    public MessageEventModel setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public MessageEventModel setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public MessageEventModel setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMsgType() {
        return msgType;
    }

    public MessageEventModel setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MessageEventModel setContent(String content) {
        this.content = content;
        return this;
    }

    public Long getMsgId() {
        return msgId;
    }

    public MessageEventModel setMsgId(Long msgId) {
        this.msgId = msgId;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public MessageEventModel setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public String getMediaID() {
        return mediaID;
    }

    public MessageEventModel setMediaID(String mediaID) {
        this.mediaID = mediaID;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public MessageEventModel setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getRecognition() {
        return recognition;
    }

    public MessageEventModel setRecognition(String recognition) {
        this.recognition = recognition;
        return this;
    }

    public Long getM() {
        return m;
    }

    public MessageEventModel setM(Long m) {
        this.m = m;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public MessageEventModel setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public MessageEventModel setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
        return this;
    }

    public String getLocationX() {
        return locationX;
    }

    public MessageEventModel setLocationX(String locationX) {
        this.locationX = locationX;
        return this;
    }

    public String getLocationY() {
        return locationY;
    }

    public MessageEventModel setLocationY(String locationY) {
        this.locationY = locationY;
        return this;
    }

    public String getScale() {
        return scale;
    }

    public MessageEventModel setScale(String scale) {
        this.scale = scale;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public MessageEventModel setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MessageEventModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MessageEventModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MessageEventModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTicket() {
        return ticket;
    }

    public MessageEventModel setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public MessageEventModel setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public MessageEventModel setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getPrecision() {
        return precision;
    }

    public MessageEventModel setPrecision(String precision) {
        this.precision = precision;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public MessageEventModel setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getEventKey() {
        return eventKey;
    }

    public MessageEventModel setEventKey(String eventKey) {
        this.eventKey = eventKey;
        return this;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public MessageEventModel setMusicURL(String musicURL) {
        this.musicURL = musicURL;
        return this;
    }

    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    public MessageEventModel setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
        return this;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public MessageEventModel setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
        return this;
    }

    public String getArticles() {
        return articles;
    }

    public MessageEventModel setArticles(String articles) {
        this.articles = articles;
        return this;
    }

}
