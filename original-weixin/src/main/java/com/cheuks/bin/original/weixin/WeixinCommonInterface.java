package com.cheuks.bin.original.weixin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cheuks.bin.original.weixin.mp.contant.WeixinUrlContant;
import com.cheuks.bin.original.weixin.mp.model.request.AccessTokenRequest;
import com.cheuks.bin.original.weixin.mp.model.request.CreateMenuRequest;
import com.cheuks.bin.original.weixin.mp.model.request.QrCodeRequest;
import com.cheuks.bin.original.weixin.mp.model.request.WeiXinUserInfoRequest;
import com.cheuks.bin.original.weixin.mp.model.response.QrCodeResponse;
import com.cheuks.bin.original.weixin.mp.model.response.WeiXinUserInfoResponse;

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
     * @return
     * @throws Throwable
     */
    String getAccessToken(String appid, String secret, String grantType) throws Throwable;

    /***
     * accessTokey 缓存键名
     * 
     * @param appid
     * @param secret
     * @param grantType
     * @return
     */
    String tokenCacheKeyName(String appid, String secret, String grantType);

    /***
     * 获取accessToken
     * 
     * @param accessToken
     * @return
     * @throws Throwable
     */
    String getAccessToken(AccessTokenRequest accessToken, boolean checkCache) throws Throwable;

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

}
