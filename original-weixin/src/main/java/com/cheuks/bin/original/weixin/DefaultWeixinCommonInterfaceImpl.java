package com.cheuks.bin.original.weixin;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.util.ConfigManager;
import com.cheuks.bin.original.common.util.HttpClientUtil;
import com.cheuks.bin.original.weixin.mp.model.AccessToken;
import com.cheuks.bin.original.weixin.mp.model.Menu;
import com.cheuks.bin.original.weixin.mp.model.QrCode;
import com.cheuks.bin.original.weixin.mp.model.WeiXinUserInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DefaultWeixinCommonInterfaceImpl implements WeixinCommonInterface {

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    private ConfigManager configManager;

    private ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    private HttpClientUtil httpClientUtil = HttpClientUtil.newInstance();

    public String getAccessToken(String appid, String secret, String grantType) throws Throwable {
        String result = redisFactory.get(tokenCacheKeyName(appid, secret, grantType));
        if (null == result) {
            String url = (String) configManager.getConfig(MP_GET_ACCESS_TOKEN);
            String params = configManager.objectToUrlParams(new AccessToken(appid, secret, grantType), true);
            ByteArrayOutputStream out = httpClientUtil.GET(url + "?" + params, 5000, false);
            AccessToken accessToken = objectMapper.readValue(out.toByteArray(), AccessToken.class);
            if ("0".equals(accessToken.getErrCode())) {
                redisFactory.set(tokenCacheKeyName(appid, secret, grantType), accessToken.getAccessToken(), 7100);
                return accessToken.getAccessToken();
            }
            throw new Throwable("获取TOKEN失败。");
        }
        return result;
    }

    public AccessToken getAccessToken(AccessToken accessToken) throws Throwable {
        return null;
    }

    public WeiXinUserInfo getWeiXinUserInfo(String accessToken, String openId) {
        return null;
    }

    public Menu createMenu(String accessToken, Menu menu) {
        return null;
    }

    public QrCode createQrCode(String accessToken, QrCode cQrCode) {
        return null;
    }

    public String tokenCacheKeyName(String appid, String secret, String grantType) {
        return null;
    }

    public String qrCacheKeyName() {
        try {

            return null;
        } finally {

        }
    }

}
