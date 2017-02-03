package com.cheuks.bin.original.reflect.rmi;

import com.cheuks.bin.original.common.registrationcenter.RegistrationFactory;

/***
 * 
 * @author BenPack
 *
 */
public class RegisterServiceHandler {

	private static final RegisterServiceHandler REGISTER_SERVICE_HANDLER = new RegisterServiceHandler();

	private volatile boolean isStart;

	public static final RegisterServiceHandler newInstance() {
		return REGISTER_SERVICE_HANDLER;
	}

	private RegistrationFactory registrationFactory;

	private RegisterServiceHandler() {
	}

	protected void ledderService() {
		if (isStart)
			return;
		isStart = true;
		//运行
	}

	public void register() throws Throwable {
		registrationFactory.createService("/rmi", eventListener)
	}

}
