package com.cheuks.bin.original.oauth.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.cheuks.bin.original.oauth.security.service.AccountDetailsService;

public class OauthAuthenticationProvider implements AuthenticationProvider {

	@SuppressWarnings("unused")
	private AccountDetailsService userDetailsService;

	public OauthAuthenticationProvider(AccountDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//		throw new UsernameNotFoundException("利害了哥");
		return new OauthAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
		//		AccountEntity user = (AccountEntity) authentication.getPrincipal();
		//		if (null == user)
		//			throw new LogicException(I18nMsg.I18N_ACCOUNT_NOT_EXISTS_101);
		//		ServletModel servletModel = (ServletModel) authentication.getCredentials();
		//		if (null == user.getMachineCode()) {
		//			user.setMachineCode(servletModel.getRequest().getHeader(HeaderType.MACHINE_CODE));
		//		}
		//		boolean isTest = user.getName().equals("00000000000")||user.getName().equals("11111111111")||user.getName().equals("13700000000");
		//		try {
		//			//		String password = authentication.getCredentials().toString();
		//			// 认证逻辑
		//			AccountEntity userDetails = (AccountEntity) userDetailsService.loadUserByUsername(user.getName());
		//			boolean isPassword;
		//			if (null != userDetails) {
		//				if (isPassword = (null == user.getPassword()) && null == user.getVerificationCode()) {
		//					//密码、验证码
		//					throw new LogicException(I18nMsg.I18N_LOGIN_REQUIRED_PASSWORD_OR_VERIFCATION_CODE_117);
		//				} else if (null != user.getVerificationCode()) {
		//					try {
		//						if (user.getVerificationCode().equals(servletModel.getTokenFactory().getVerificationCode(user.getName()))) {
		//							//更新机器码
		//							userDetailsService.getAccountService().modifyMachineCode(userDetails.getId(), user.getMachineCode());
		//							try {
		//								servletModel.getTokenFactory().cleanToken(Long.toString(user.getId()));
		//							} catch (Throwable e) {
		//							}
		//							return new AccountAuthenticationToken(userDetails.setMachineCode(user.getMachineCode()), servletModel, userDetails.getAuthorities());
		//						}
		//					} catch (RedisExcecption e) {
		//						//验码为空
		//						throw new LogicException(I18nMsg.I18N_CAN_NOT_FOUND_RECORD_3);
		//					}
		//					//验证码不一致
		//					throw new LogicException(I18nMsg.I18N_VERIFICATION_CODE_ERROR_113);
		//
		//				} else if (isTest || userDetails.getMachineCode().equals(user.getMachineCode())) {
		//					if (!isPassword) {
		//						if (userDetails.getPassword().equals(user.getPassword())) {
		//							try {
		//								servletModel.getTokenFactory().cleanToken(Long.toString(user.getId()));
		//							} catch (Throwable e) {
		//							}
		//							return new AccountAuthenticationToken(userDetails, servletModel, userDetails.getAuthorities());
		//						} else {
		//							//密码不一致
		//							throw new LogicException(I18nMsg.I18N_ACCOUNT_PASSWORD_102);
		//						}
		//					}
		//				} else {
		//					//机器码不一致
		//					throw new LogicException(I18nMsg.I18N_MACHINE_CODE_INCONSISTENCIES_103);
		//				}
		//
		//			}
		//			return null;
		//		} catch (LogicException e) {
		//			throw e;
		//		} catch (Throwable e) {
		//			throw new LogicException(I18nMsg.I18N_ACCOUNT_NOT_EXISTS_101);
		//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(OauthAuthenticationToken.class);
	}

}
