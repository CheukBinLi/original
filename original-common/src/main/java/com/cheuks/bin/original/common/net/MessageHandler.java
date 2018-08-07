package com.cheuks.bin.original.common.net;

import java.io.Serializable;

/***
 * 
 * @author BIN
 *
 * @param <INPUT>
 *            channel
 * @param <MODEL>
 *            标准对象
 * @param <TYPE>
 *            处理类型
 */
public interface MessageHandler<INPUT, MODEL extends Serializable, TYPE> {

	Codec<INPUT, MODEL> getCodec();

	/***
	 * 多处理器区分权重
	 * 
	 * @return
	 */
	default Integer weight() {
		return 0;
	};

	/***
	 * 处理类型
	 * 
	 * @return
	 */
	TYPE getType();

	/***
	 * 反序列化对象
	 * 
	 * @param input
	 *            管道
	 * @param len
	 *            管道读取深度
	 * @throws Exception
	 */
	void doHandle(INPUT input, MODEL model) throws Exception;

}
