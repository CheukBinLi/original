package com.cheuks.bin.original.weixin;

import com.cheuks.bin.original.weixin.mp.contant.WeixinUrlContant;
import com.cheuks.bin.original.weixin.mp.model.AccessToken;
import com.cheuks.bin.original.weixin.mp.model.Menu;
import com.cheuks.bin.original.weixin.mp.model.QrCode;
import com.cheuks.bin.original.weixin.mp.model.WeiXinUserInfo;

public interface WeixinCommonInterface extends WeixinUrlContant {

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
    AccessToken getAccessToken(AccessToken accessToken) throws Throwable;

    /***
     * 获取用户信息
     * 
     * @param accessToken
     * @param openId
     * @return
     */
    WeiXinUserInfo getWeiXinUserInfo(String accessToken, String openId);

    /***
     * 创建自定义菜单
     * 
     * @param accessToken
     * @param menu
     * @return
     */
    Menu createMenu(String accessToken, Menu menu);

    /***
     * 生成带参二维码
     * 
     * @param accessToken
     * @param cQrCode
     * @return
     */
    QrCode createQrCode(String accessToken, QrCode cQrCode);

    /***
     * 二维码缓存键名
     * 
     * @return
     */
    String qrCacheKeyName();

}
