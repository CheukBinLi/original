package com.cheuks.bin.original.oauth.util;

import java.util.Date;
import java.util.HashSet;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cheuks.bin.original.common.exception.OverdueException;
import com.cheuks.bin.original.oauth.model.AuthInfo;
import com.cheuks.bin.original.oauth.model.KeyModel;

public class TokenFactory {

	interface AuthInfoField {
		String ID = "id", TYPE = "type", USER_NAME = "name", DEVICE_CODE = "code", ROLE = "role";
	}

	Algorithm getAlgorithm(KeyModel key) {
		switch (key.getEncryptionType()) {
		case HMAC256:
			return Algorithm.HMAC256(key.getSecret());
		case HMAC384:
			return Algorithm.HMAC384(key.getSecret());
		case HMAC512:
			return Algorithm.HMAC512(key.getSecret());

		default:
			throw new RuntimeException("not support the encryption type [" + key.getEncryptionType().toString() + "].");
		}
	}

	public String generateToken(AuthInfo auth, KeyModel key, long expireSeconds) throws Throwable {
		Algorithm algorithm = getAlgorithm(key);
		String type = auth.getType().trim().toUpperCase();
		JWTCreator.Builder builder = JWT.create().withIssuer(key.getIssuer()).withExpiresAt(new Date(System.currentTimeMillis() + (expireSeconds * 1000)));
		builder.withClaim(AuthInfoField.ID, auth.getId());
		builder.withClaim(AuthInfoField.USER_NAME, auth.getUserName());
		builder.withClaim(AuthInfoField.DEVICE_CODE, auth.getDeviceCode());
		builder.withArrayClaim(AuthInfoField.ROLE, auth.getRole().toArray(new String[0]));
		return type + ":" + builder.sign(algorithm);
	}

	public AuthInfo parser(String token, KeyModel key) throws OverdueException, Exception {
		Algorithm algorithm = getAlgorithm(key);
		algorithm = Algorithm.HMAC256(key.getSecret());
		JWTVerifier verifier = JWT.require(algorithm).withIssuer(key.getIssuer()).build();
		DecodedJWT jwt = verifier.verify(token);
		Date expire = jwt.getExpiresAt();
		if (null == expire || expire.getTime() <= System.currentTimeMillis()) {
			throw new OverdueException();
		}
		return new AuthInfo()
							.setId(jwt.getClaim(AuthInfoField.ID).asString())
							.setUserName(jwt.getClaim(AuthInfoField.USER_NAME).asString())
							.setDeviceCode(jwt.getClaim(AuthInfoField.DEVICE_CODE).asString())
							.setRole(new HashSet<String>(jwt.getClaim(AuthInfoField.ROLE).asList(String.class)))
							.setType(token.substring(0, token.indexOf(":")));
	}
}
