package com.cheuks.bin.original.oauth.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthBasicAuthenticationFilter extends BasicAuthenticationFilter {

	public OauthBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.err.println("xxxxxxxxxxxxxx");
		//		String token = request.getHeader(AUTHORIZATION);
		//
		//		if (null == token || !token.startsWith(TOKEN_TYPE)) {
		//			chain.doFilter(request, response);
		//			return;
		//		}
		//		token = token.substring(TOKEN_TYPE.length());
		//		AccountEntity accountEntity;
		//		try {
		//			accountEntity = tokenFactory.parserToken(token);
		//		} catch (Throwable e) {
		//			ExceptionPrinterUtil.instance().write(response, resultFactory.create(I18nMsg.I18N_TOKEN_EXPIRED_105), null);
		//			return;
		//		}
		//
		//		boolean isTest = accountEntity.getName().equals("00000000000") || accountEntity.getName().equals("11111111111") || accountEntity.getName().equals("13700000000");
		//		String machineCode = request.getHeader(MACHINE_CODE);
		//		if (null == machineCode) {
		//			ExceptionPrinterUtil.instance().write(response, resultFactory.create(I18nMsg.I18N_MACHINE_CODE_IS_NULL_109), null);
		//			return;
		//		}
		//		//token过期
		//		if (tokenFactory.tokenIsExpire(accountEntity)) {
		//			ExceptionPrinterUtil.instance().write(response, resultFactory.create(I18nMsg.I18N_TOKEN_EXPIRED_105), null);
		//			return;
		//		} else if (!isTest && !accountEntity.getMachineCode().equals(machineCode)) {//机器码不一致
		//			ExceptionPrinterUtil.instance().write(response, resultFactory.create(I18nMsg.I18N_MACHINE_CODE_INCONSISTENCIES_103), null);
		//			return;
		//		}
		//
		//		try {
		//			accountEntity = tokenFactory.getTokenInfoByToken(token);
		//		} catch (Throwable e) {
		//			ExceptionPrinterUtil.instance().write(response, resultFactory.create(I18nMsg.I18N_TOKEN_EXPIRED_105), null);
		//			return;
		//		}
//		User user=new User("test", "123456");
		SecurityContextHolder.getContext().setAuthentication(new OauthAuthenticationToken(new User("test", "123456"), null));

		chain.doFilter(request, response);
	}

}
