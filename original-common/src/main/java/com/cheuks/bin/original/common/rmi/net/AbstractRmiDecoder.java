package com.cheuks.bin.original.common.rmi.net;

import com.cheuks.bin.original.common.cache.CacheException;
import com.cheuks.bin.original.common.cache.CacheSerialize;
import com.cheuks.bin.original.common.rmi.RmiException;

/***
 * 
 * @Title: original-rmi
 * @Description: RMI解码器
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年4月28日 下午12:03:35
 *
 */
public abstract class AbstractRmiDecoder<INPUT> {

	private CacheSerialize cacheSerialize;

	public abstract byte[] extractData(INPUT input) throws RmiException;

	public final <T> T decode(INPUT input) throws RmiException {
		try {
			return cacheSerialize.decodeT(extractData(input));
		} catch (CacheException e) {
			throw new RmiException(e);
		}
	}

	public AbstractRmiDecoder<INPUT> setCacheSerialize(CacheSerialize cacheSerialize) {
		this.cacheSerialize = cacheSerialize;
		return this;
	}

}
