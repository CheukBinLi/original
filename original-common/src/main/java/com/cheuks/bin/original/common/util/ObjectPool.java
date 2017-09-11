package com.cheuks.bin.original.common.util;

import java.util.NoSuchElementException;

public interface ObjectPool<T> {

	int STATUS_NORNAL = 0;
	int STATUS_BUSYL = 1;
	int STATUS_IDLE = 2;

	/***
	 * 借出对象
	 * 
	 * @return
	 * @throws Exception
	 * @throws NoSuchElementException
	 * @throws IllegalStateException
	 */
	T borrowObject() throws Exception, NoSuchElementException, IllegalStateException;

	/***
	 * 归还对象
	 * 
	 * @param t
	 * @throws Exception
	 */
	void returnObject(T t) throws Exception;

	/***
	 * 过期对象
	 * 
	 * @param t
	 * @throws Exception
	 */
	void invalidateObject(T t) throws Exception;

	/***
	 * 检测失效标记,失效直接移除
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	boolean isFailure(T t) throws Exception;

	/***
	 * 添加对象
	 * 
	 * @param t
	 * @throws Exception
	 * @throws IllegalStateException
	 * @throws UnsupportedOperationException
	 */
	void addObject(T t) throws Exception, IllegalStateException, UnsupportedOperationException;

	/***
	 * 待借出对象数量
	 * 
	 * @return
	 */
	int getNumIdle();

	/***
	 * 借出对象数量
	 * 
	 * @return
	 */
	int getNumActive();

	/***
	 * 重空重置
	 * 
	 * @throws Exception
	 */
	void reset() throws Exception;

	/***
	 * 销毁对象
	 * 
	 * @param t
	 * @throws Exception
	 */
	void destroyObject(T t) throws Exception;

	void shutdown();
}
