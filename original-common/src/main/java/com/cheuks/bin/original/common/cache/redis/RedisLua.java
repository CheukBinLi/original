package com.cheuks.bin.original.common.cache.redis;

import java.io.IOException;

public interface RedisLua {

	/***
	 * 加载脚本
	 * 
	 * @param filtPaths
	 * @throws IOException
	 * @throws RedisExcecption
	 */
	void initLoader(String... filePaths) throws IOException, RedisExcecption;

	/**
	 * 获取 脚本 SHA
	 * 
	 * @param name
	 * @return
	 */
	String getSha(String name);

	/***
	 * 清空脚本
	 * 
	 * @throws RedisExcecption
	 */
	void clear() throws RedisExcecption;

}