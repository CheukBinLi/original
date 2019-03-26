package com.cheuks.bin.original.oauth.security.token.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.oauth.model.AuthInfo;
import com.cheuks.bin.original.oauth.model.Role;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.model.UserDetail;
import com.cheuks.bin.original.oauth.model.encryption.KeyModel;
import com.cheuks.bin.original.oauth.model.encryption.KeyModel.EncryptionType;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;
import com.cheuks.bin.original.oauth.security.config.Constant;
import com.cheuks.bin.original.oauth.security.token.FilterChain;
import com.cheuks.bin.original.oauth.util.JWTUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractTokenHandler implements TokenHandler, Constant {

	/***
	 * 分割符
	 */
	private String delimiter = ":";

	@Autowired
	private RedisFactory redisFactory;

	protected String issuer = "token_type";// 发行商，source
	protected String secret = "@default_secret@";// 加密盐
	protected EncryptionType encryptionType = EncryptionType.HMAC256;
	protected long expireSeconds = 604800;// 7天过期
	
	/***
	 * 登陆
	 * 
	 * @param user
	 * @param pass
	 * @return
	 * @throws Throwable
	 */
	public abstract UserDetail login(String user, String pass, String verificationCode) throws Throwable;

	/***
	 * 状态机机制方法（记录用户登录信息）
	 * 
	 * @param token
	 * @param luggage
	 * @return
	 */
	public Locker set(AuthInfo authInfo, Object luggage) throws Throwable {
		// if (redisFactory.setOO(generateCheckInName(authInfo), luggage,
		// Long.valueOf(getExpireSeconds()).intValue()))
		Locker locker = new Locker(authInfo.getNonceStr(), luggage);
		if (redisFactory.set(generateCheckInName(authInfo).getBytes(), redisFactory.getCacheSerialize().encode(locker), Long.valueOf(getExpireSeconds()).intValue()))
			return locker;
		return null;
	}

	public Locker set(String token, Object luggage) throws Throwable {
		return set(decode(token), luggage);
	}

	public Locker get(AuthInfo authInfo) throws Throwable {
		byte[] data;
		if (null == (data = redisFactory.get(generateCheckInName(authInfo).getBytes())))
			return null;
		return redisFactory.getCacheSerialize().decodeT(data, Locker.class);
	}
	
	public Locker get(User user) throws Throwable {
		byte[] data;
		if (null == (data = redisFactory.get(generateCheckInName(user).getBytes())))
			return null;
		return redisFactory.getCacheSerialize().decodeT(data, Locker.class);
	}

	public Locker get(String token) throws Throwable {
		return get(decode(token));
	}

	@Override
	public String refreshToken(AuthInfo authInfo) throws Throwable {
		if (redisFactory.expire(generateCheckInName(authInfo), Long.valueOf(getExpireSeconds()).intValue()))
			return "SUCCESS";
		return "FAIL";
	}

	@Override
	public String refreshToken(String token) throws Throwable {
		return refreshToken(decode(token));
	}

	public String generateCheckInName(AuthInfo authInfo) {
		return authInfo.getSource() + ":" + authInfo.getId();
	}

	public String generateCheckInName(User user) {
		return user.getSource() + ":" + user.getUnid();
	}

	@Override
	public String encode(AuthInfo authInfo) throws Throwable {
		String token = JWTUtil.generateToken(authInfo, new KeyModel(issuer, secret, encryptionType), expireSeconds);
		return token;
	}

	@Override
	public AuthInfo decode(String token) throws Throwable {
		return JWTUtil.parser(token, new KeyModel(issuer, secret, encryptionType));
	}

	@Override
	public boolean isSupport(String t) {
		return (!StringUtil.isEmpty(t) && t.startsWith(getType()));
	}

	@Override
	public Authentication doAnalysis(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Throwable {
		// 获取TOKEN方法
		String token;
		if (isSupport(token = request.getHeader(AUTHORIZATION))) {
			return analysisToken(analysisToken(token, getDelimiter()), request, response);
		}

		if (null != chain)
			return chain.doAnalysis(request, response);
		return null;
	}

	@Override
	public User getUser(AuthInfo authInfo) throws Throwable {
		return new User().setUnid(authInfo.getId()).setSource(authInfo.getSource()).setTenant(authInfo.getTenant()).setRoles(new Role().appendGranted(authInfo.getRole()));
	}

	@Override
	public User getUserDetail(String token) throws Throwable {
		return getUser(decode(token));
	}

	@Override
	public void doLogout(HttpServletRequest request, HttpServletResponse response, String token, FilterChain chain) throws Throwable {
		if (isSupport(request.getHeader(AUTHORIZATION))) {
			logout(request, response, token);
		}
		if (null != chain)
			chain.doLogout(request, response, token);
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, String token) throws Throwable {
		redisFactory.delete(generateCheckInName(decode(token)).getBytes());
	}

	@Override
	public boolean singleSignOnCheck(OauthAuthenticationToken authentication) throws Throwable {
		// 校验随机码是否一致
		return get(authentication.getUserDetail().getUserInfo()).getVersion().equals(authentication.getUserDetail().getVerificationCode());
//		return getLuggage(authInfo).equals(authInfo.getUserDetail().getVerificationCode());
	}
	
}
