package com.cheuks.bin.original.oauth.util;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import lombok.extern.slf4j.Slf4j;

/***
 * * CREATE ON 2018年06月04 下午4:37:37 EMAIL:20796698@QQ.COM
 *
 * @author CHEUK.BIN.LI
 * @see 初始化错误码
 */
@Slf4j
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ResultFactory implements DefaultResultCode {

	private final transient Map<String, String> CACHE = new ConcurrentSkipListMap<>();

	private final Result SUCCESS_RESULT = new Result<Object>(SUCCESS, SUCCESS_MSG, null);

	private final Result FAIL_RESULT = new Result<Object>(FAIL, FAIL_MSG, null);

	public <T extends Object> Result<T> createSuccess(T t) {
		return create(SUCCESS, null, t);
	}

	public <T extends Object> Result<T> createSuccess() {
		return (Result<T>) create(SUCCESS);
	}

	public <T extends Object> Result<T> create(String code, String defaultValue, T t) {
		String result = CACHE.get(code);
		return new Result<T>(code, null == result ? defaultValue : result, t);
	}

	public <T extends Object> Result<T> create(String code, String defaultValue) {
		return create(code, defaultValue, null);
	}

	public <T> Result<T> create(String code) {
		switch (code) {

		case SUCCESS:
			return SUCCESS_RESULT;

		case FAIL:
			return FAIL_RESULT;

		default:
			return create(code, "", null);
		}
	}

	public <T> Result<T> create(final Throwable e) {
		boolean isLogicException = e instanceof LogicException;
		boolean hasCode = false;
		try {
			String code = e.getMessage();
			String result = isLogicException ? formatMsg(CACHE.get(code), ((LogicException) e).getMsg()) : CACHE.get(code);
			return (hasCode = null == result) ? new Result<T>(FAIL, code, null) : new Result<T>(code, result, null);
		} finally {
			if (!isLogicException || !hasCode)
				log.error(e.getMessage(), e);
		}
	}

	public void addMessage(Map<String, String> msg) {
		CACHE.putAll(msg);
	}

	//		public static void main(String[] args) {
	//			ResultFactory rf = new ResultFactory();
	//			rf.CACHE.put("11011", "{}那边的小朋友{}，请你吃糖好唔好？{}");
	//			Result<?> x = rf.create(new LogicException("11011", "喂", "听到吗？", "使不得使得"));
	//			System.err.println(x.getMsg());
	//		}
}
