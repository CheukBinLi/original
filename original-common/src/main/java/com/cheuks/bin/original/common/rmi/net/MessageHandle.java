package com.cheuks.bin.original.common.rmi.net;

import com.cheuks.bin.original.common.rmi.RmiContent;

/***
 * 
 * @Title: original-rmi
 * @Description: 消息处理接口
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年4月28日 下午3:36:26
 *
 */
public interface MessageHandle<Input, Value> extends RmiContent {

	/***
	 * 处理的服务类型
	 * 
	 * @return
	 */
	int serverType();

	/***
	 * 事件处理
	 * 
	 * @param i
	 * @param v
	 */
	void doHandle(Input i, Value v);

}
