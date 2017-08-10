package com.cheuks.bin.original.weixin.mp.contant;

public interface WeixinUrlContant {

    /** 获取公众号ACCESS_TOKEN */
    String MP_GET_GET_ACCESS_TOKEN = "mp.get.get.AccessToken";

    /***/
    String MP_POST_CREATE_MENU = "mp.post.create.Menu";

    String MP_GET_CLEAN_MENU = "mp.get.clean.Menu";

    String MP_POST_CREATE_QR_CODE = "mp.post.create.QrCode";

    String MP_GET_QR_CODE = "mp.get.QrCode";

    String MP_GET_GET_WEI_XIN_USER_INFO = "mp.get.get.WeixinUserInfo";

    public static interface MessageType {

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

}
