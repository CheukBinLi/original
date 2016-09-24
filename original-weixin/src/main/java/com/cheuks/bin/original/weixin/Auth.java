package com.cheuks.bin.original.weixin;

import java.io.IOException;

import com.cheuks.bin.original.weixin.util.HttpClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Auth {

	// public void auth(String appId, String redirectUrl, String params) throws IOException {
	// String path = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=SCOPE&state=%s#wechat_redirect", appId, URLDecoder.decode(redirectUrl, "utf-8"), params);
	// HttpClient hc = new HttpClient();
	// hc.Get(path, 3000, true, true);
	// }

	private final JsonParser jsonParser = new JsonParser();

	public JsonObject auth(String appId, String appSecret, String code) throws IOException {

		// JsonObject auth = new Auth().auth("wxdae69667d240c3bf", "367e8e171515e9357b59d8a9e960f9b3", code);
		// System.out.println("1:" + (System.currentTimeMillis() - now));
		// now = System.currentTimeMillis();
		// Map<String, String> map = AuthUtil.getWebLoginAuth("wxdae69667d240c3bf", "367e8e171515e9357b59d8a9e960f9b3", code);
		// System.out.println("2:" + (System.currentTimeMillis() - now));
		// String state = request.getParameter("state");

		String path = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", appId, appSecret, code);
		HttpClient hc = new HttpClient();
		String json = hc.Get(path, 3000, true, false);
		JsonObject jsonObject = (JsonObject) jsonParser.parse(json);

		return jsonObject;
	}

}
