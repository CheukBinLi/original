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
	
	/***
	 * 增加脚本
	 * @param script
	 */
	RedisScript appendScript(Script... script);
	
	/***
	 * 加载脚本
	 * @param slotKey {1}:a{2}
	 * @param script
	 * @return
	 * @throws RedisExcecption
	 */
	String scriptLoad(String slotKey, String script) throws RedisExcecption;

	/***
	 * 加载脚本
	 * @param script
	 * @param keys
	 * @return
	 * @throws RedisExcecption
	 */
	String scriptLoad(Script script, String... keys) throws RedisExcecption;
	 	
	/***
	 * 动态脚本生成
	 * @param scriptName
	 * @param keys
	 * @param keysAndArgs
	 * @return
	 * @throws RedisExcecption
	 */
	Object evalShaByScript(String scriptName, int keys, String... keysAndArgs) throws RedisExcecption;

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
