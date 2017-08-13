package com.cheuks.bin.original.weixin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.util.ConfigManager;
import com.cheuks.bin.original.common.util.HttpClientUtil;
import com.cheuks.bin.original.common.weixin.mp.MessageEventHandleManager;
import com.cheuks.bin.original.common.weixin.mp.model.MessageEventModel;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
import com.cheuks.bin.original.weixin.mp.model.api.request.AccessTokenRequest;
import com.cheuks.bin.original.weixin.mp.model.api.request.CreateMenuRequest;
import com.cheuks.bin.original.weixin.mp.model.api.request.QrCodeRequest;
import com.cheuks.bin.original.weixin.mp.model.api.request.WeiXinUserInfoRequest;
import com.cheuks.bin.original.weixin.mp.model.api.response.AccessTokenResponse;
import com.cheuks.bin.original.weixin.mp.model.api.response.CreateMenuResponse;
import com.cheuks.bin.original.weixin.mp.model.api.response.QrCodeResponse;
import com.cheuks.bin.original.weixin.mp.model.api.response.WeiXinUserInfoResponse;
import com.cheuks.bin.original.weixin.mp.model.customservice.message.BaseMessage;
import com.cheuks.bin.original.weixin.mp.model.customservice.message.TextMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public class DefaultWeixinCommonInterfaceImpl implements WeixinCommonInterface, ApplicationContextAware, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultWeixinCommonInterfaceImpl.class);

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    private ConfigManager configManager;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @Autowired
    private MessageEventHandleManager messageEventHandleManager;

    private HttpClientUtil httpClientUtil = HttpClientUtil.newInstance();

    class MessageHandleManager {

    }

    public void afterPropertiesSet() throws Exception {
        messageEventHandleManager.start();
    }

    public void weixinTokenVerification(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String result = request.getParameter("echostr");
        if (null != result) {
            response.getWriter().write(result);
            return;
        }

        // 读取流
        InputStream in = request.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        //关闭即成功
        out.close();
        in.close();
        //处理
        MessageEventModel model = messageEventHandleManager.pushMessage(out.toByteArray());
        sendMessage(getAccessTokenByWeixinCode(model.getToUserName()), new TextMessage(model.getFromUserName(), "已接收你的消息。"));
    }

    public String getAccessToken(String appId, String secret, String grantType, String weixinCode) throws Throwable {
        String result = redisFactory.get(tokenCacheKeyName(appId, secret, grantType, weixinCode));
        if (null == result) {
            AccessTokenRequest request = new AccessTokenRequest(appId, secret);
            if (null != grantType)
                request.setGrantType(grantType);
            result = getAccessToken(request, weixinCode, false);
        }
        return result;
    }

    public String getAccessToken(AccessTokenRequest accessToken, String weixinCode, boolean checkCache) throws Throwable {
        String result = null;
        if (checkCache)
            result = redisFactory.get(tokenCacheKeyName(accessToken.getAppId(), accessToken.getSecret(), accessToken.getGrantType(), weixinCode));
        if (null == result) {
            String url = (String) configManager.getConfig(MP_GET_GET_ACCESS_TOKEN);
            String params = configManager.objectToUrlParams(accessToken, true);
            ByteArrayOutputStream out = httpClientUtil.GET(url + "?" + params, 5000, false);
            AccessTokenResponse accessTokenResponse = objectMapper.readValue(out.toByteArray(), AccessTokenResponse.class);
            if (null != accessTokenResponse.getErrCode()) {
                throw new Throwable(String.format("获取TOKEN失败，错误码<%d>,错误信息:%s", accessTokenResponse.getErrCode(), accessTokenResponse.getErrMsg()));
            }
            result = accessTokenResponse.getAccessToken();
            redisFactory.set(tokenCacheKeyName(accessToken.getAppId(), accessToken.getSecret(), accessToken.getGrantType(), weixinCode), result, 7100);
            return result;
        }
        return result;
    }

    public WeiXinUserInfoResponse getWeiXinUserInfo(WeiXinUserInfoRequest weiXinUserInfoRequest) throws Throwable {
        String url = (String) configManager.getConfig(MP_GET_GET_WEI_XIN_USER_INFO);
        String params = configManager.objectToUrlParams(weiXinUserInfoRequest, true);
        ByteArrayOutputStream out = httpClientUtil.GET(url + "?" + params, 5000, false);
        System.out.println(new String(out.toByteArray()));
        WeiXinUserInfoResponse weiXinUserInfoResponse = objectMapper.readValue(out.toByteArray(), WeiXinUserInfoResponse.class);
        if (null != weiXinUserInfoResponse.getErrCode()) {
            throw new Throwable(String.format("获取用户信息失败,错误码<%d>,错误信息:%s", weiXinUserInfoResponse.getErrCode(), weiXinUserInfoResponse.getErrMsg()));
        }
        return weiXinUserInfoResponse;
    }

    public void createMenu(String accessToken, CreateMenuRequest menu) throws Throwable {
        createMenu(accessToken, configManager.objectToUrlParams(menu, true));
    }

    public void createMenu(String accessToken, String menu) throws Throwable {
        // 删除菜单
        httpClientUtil.GET(configManager.getConfig(MP_GET_CLEAN_MENU) + accessToken, 5000, true);
        // 创建菜单
        ByteArrayOutputStream out = httpClientUtil.POST((String) configManager.getConfig(MP_POST_CREATE_MENU) + accessToken, menu, 5000, false);
        CreateMenuResponse createMenuResponse = objectMapper.readValue(out.toByteArray(), CreateMenuResponse.class);
        if (0 != createMenuResponse.getErrCode()) {
            throw new Throwable(String.format("创建菜单失败,错误码<%d>,错误信息:%s", createMenuResponse.getErrCode(), createMenuResponse.getErrMsg()));
        }
    }

    public QrCodeResponse createQrCode(String accessToken, QrCodeRequest cQrCode) throws Throwable {
        ByteArrayOutputStream out = httpClientUtil.POST((String) configManager.getConfig(MP_POST_CREATE_QR_CODE) + accessToken, objectMapper.writeValueAsString(cQrCode), 5000, false);
        QrCodeResponse qrCodeResponse = objectMapper.readValue(out.toByteArray(), QrCodeResponse.class);
        if (null != qrCodeResponse.getErrCode()) {
            throw new Throwable(String.format("创建二维码失败,错误码<%d>,错误信息:%s", qrCodeResponse.getErrCode(), qrCodeResponse.getErrMsg()));
        }
        return qrCodeResponse;
    }

    public String tokenCacheKeyName(String appid, String secret, String grantType, String weixinCode) {
        //        return "winxinAK:" + appid + "_" + secret;
        return "winxinCode:" + weixinCode;
    }

    public String qrCacheKeyName() {
        try {
            return null;
        } finally {

        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            objectMapper = applicationContext.getBean(ObjectMapper.class);
        } catch (Exception e) {
            objectMapper = new ObjectMapper();
        }
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public RedisFactory getRedisFactory() {
        return redisFactory;
    }

    public DefaultWeixinCommonInterfaceImpl setRedisFactory(RedisFactory redisFactory) {
        this.redisFactory = redisFactory;
        return this;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DefaultWeixinCommonInterfaceImpl setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
        return this;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DefaultWeixinCommonInterfaceImpl setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public MessageEventHandleManager getMessageEventHandleManager() {
        return messageEventHandleManager;
    }

    public DefaultWeixinCommonInterfaceImpl setMessageEventHandleManager(MessageEventHandleManager messageEventHandleManager) {
        this.messageEventHandleManager = messageEventHandleManager;
        return this;
    }

    public void sendMessage(String accessToken, BaseMessage baseMessage) throws Throwable {
        ByteArrayOutputStream out = httpClientUtil.POST((String) configManager.getConfig(MP_POST_SEND_MESSAGE_CUSTOM_SERVICE) + accessToken, objectMapper.writeValueAsString(baseMessage), 5000, false);
        MpBaseModel response = objectMapper.readValue(out.toByteArray(), MpBaseModel.class);
        if (0 != response.getErrCode()) {
            throw new Throwable(String.format("消息发送失败,错误码<%d>,错误信息:%s", response.getErrCode(), response.getErrMsg()));
        }

    }

    public String getAccessTokenByWeixinCode(String weixinCode) throws Throwable {
        return redisFactory.get(tokenCacheKeyName(null, null, null, weixinCode));
    }

}
