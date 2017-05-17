package com.cheuks.bin.original.common.rmi.net;

/***
 * 
 * @Title: original-rmi
 * @Description: 篡改校验接口
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年4月28日 下午12:03:20
 *
 */
public interface TamperVerification {

	/***
	 * 篡改校验
	 * 
	 * @param data 数据
	 * @param verificationCode 校验码
	 * @return 数据是否被篡改
	 */
	boolean verification(byte[] data, String verificationCode);

	/***
	 * 
	 * @param data 数据
	 * @return 校验码
	 */
	String getVerificationCode(byte[] data);

}
