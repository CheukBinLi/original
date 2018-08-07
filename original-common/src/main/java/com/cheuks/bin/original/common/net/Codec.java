package com.cheuks.bin.original.common.net;

import java.io.Serializable;

/***
 * *
 * 
 * @Title: original-common
 * @Description:解码/编码器
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2018年07月31日 下午2:56:21
 * @param <INPUT> 管道
 * @param <MODEL> 序列化对象类型
 *
 */
public interface Codec<INPUT, MODEL extends Serializable> {

	/***
	 * 反序列化类型
	 * 
	 * @return
	 */
	Class<MODEL> getType();

	/***
	 * 序列化
	 * 
	 * @param serializable
	 *            对象
	 * @return
	 * @throws Exception
	 */
	byte[] encode(MODEL serializable) throws Exception;

	/***
	 * 
	 * @param input
	 *            管道
	 * @param len
	 *            读取长度
	 * @return
	 * @throws Exception
	 */
	MODEL decode(INPUT input, int len) throws Exception;

}
