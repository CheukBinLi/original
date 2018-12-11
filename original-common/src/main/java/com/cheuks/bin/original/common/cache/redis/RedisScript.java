package com.cheuks.bin.original.common.cache.redis;

import java.io.IOException;

public interface RedisScript {
	/***
	 * 加载脚本
	 * 
	 * @param filtPaths
	 * @throws IOException
	 * @throws RedisExcecption
	 */
	void initScriptLoader(String... filePaths) throws IOException, RedisExcecption;

	/***
	 * 扫描加载
	 * 
	 * @throws IOException
	 * @throws RedisExcecption
	 */
	void initScriptLoader() throws IOException, RedisExcecption;

	/**
	 * 获取 脚本 SHA
	 * 
	 * @param name
	 * @return
	 */
	String getScriptSha(String name);

	/***
	 * 清空脚本
	 * 
	 * @throws RedisExcecption
	 */
	void scriptClear() throws RedisExcecption;

	void scriptReset(boolean force, String... filePaths) throws IOException, RedisExcecption;
}
