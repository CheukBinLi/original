package com.cheuks.bin.original.weixin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cheuks.bin.original.common.weixin.content.WeixinUrlContant;
import com.cheuks.bin.original.weixin.mp.model.api.request.AccessTokenRequest;
import com.cheuks.bin.original.weixin.mp.model.api.request.CreateMenuRequest;
import com.cheuks.bin.original.weixin.mp.model.api.request.QrCodeRequest;
import com.cheuks.bin.original.weixin.mp.model.api.request.WeiXinUserInfoRequest;
import com.cheuks.bin.original.weixin.mp.model.api.response.QrCodeResponse;
import com.cheuks.bin.original.weixin.mp.model.api.response.WeiXinUserInfoResponse;
import com.cheuks.bin.original.weixin.mp.model.customservice.message.BaseMessage;

public interface WeixinCommonInterface extends WeixinUrlContant {

    /***
     * 响应微信发送的Token验证
     * 
     * @param request
     * @param response
     * @throws Throwable
     */
    void weixinTokenVerification(HttpServletRequest request, HttpServletResponse response) throws Throwable;

    /***
     * 获取accessToken
     * 
     * @param appid
     * @param secret
     * @param grantType
     * @param weixinCode 微信号： gh_5e163a12a084
     * @return
     * @throws Throwable
     */
    String getAccessToken(String appid, String secret, String grantType, String weixinCode) throws Throwable;

    /***
     * accessTokey 缓存键名
     * 
     * @param appid
     * @param secret
     * @param grantType
     * @return
     */
    String tokenCacheKeyName(String appid, String secret, String grantType, String weixinCode);

    /***
     * 获取accessToken
     * 
     * @param accessToken
     * @return
     * @throws Throwable
     */
    String getAccessToken(AccessTokenRequest accessToken, String weixinCode, boolean checkCache) throws Throwable;

    /***
     * 
     * @param weixinCode weiXinUserName 微信用户名<br>
     *            前提条件：缓存/数据库里已初始化
     * @return
     * @throws Throwable
     */
    String getAccessTokenByWeixinCode(String weixinCode) throws Throwable;

    /***
     * 获取用户信息
     * 
     * @param accessToken
     * @param openId
     * @return
     */
    WeiXinUserInfoResponse getWeiXinUserInfo(WeiXinUserInfoRequest weiXinUserInfoRequest) throws Throwable;

    /***
     * 创建自定义菜单
     * 
     * @param accessToken
     * @param menu
     * @return
     */
    void createMenu(String accessToken, CreateMenuRequest menu) throws Throwable;

    void createMenu(String accessToken, String menu) throws Throwable;

    /***
     * 生成带参二维码
     * 
     * @param accessToken
     * @param cQrCode
     * @return
     */
    QrCodeResponse createQrCode(String accessToken, QrCodeRequest cQrCode) throws Throwable;

    /***
     * 二维码缓存键名
     * 
     * @return
     */
    String qrCacheKeyName();

    /***
     * 发送消息
     * 
     * @param accessToken
     * @param baseMessage
     */
    void sendMessage(String accessToken, BaseMessage baseMessage) throws Throwable;
}
