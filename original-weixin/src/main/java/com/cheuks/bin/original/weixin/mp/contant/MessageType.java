package com.cheuks.bin.original.weixin.mp.contant;

public interface MessageType {

    /** 文字 */
    String TEXT = "text";

    /** 图片 */
    String IMAGE = "image";

    /** 语音 */
    String VOICE = "voice";

    /** 视频 */
    String VIDEO = "video";

    /** 小视频 */
    String SHORT_VIDEO = "shortvideo";

    /** 地理位置 */
    String LOCATION = "location";

    /** 连接 */
    String LINK = "link";

    /** 事件 */
    String EVENT = "event";

    /** 扫描二维码,用户未关注时，进行关注后的事件推送 */
    String EVENT_SUBSCRIBE = "subscribe";

    /** 扫描二维码,用户已关注时的事件推送 */
    String EVENT_SCAN = "SCAN";

    /** 上报地理位置事件 */
    String EVENT_LOCATION = "LOCATION";

    /** 自定义菜单事件：点击菜单拉取消息时的事件推送 */
    String EVENT_CLICK = "CLICK";

    /** 自定义菜单事件：点击菜单跳转链接时的事件推送 */
    String EVENT_VIEW = "VIEW";
}
