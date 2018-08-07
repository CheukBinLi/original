package com.cheuks.bin.original.common.net;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import com.cheuks.bin.original.common.util.conver.CollectionUtil;

public interface MessageCenter<INPUT extends Object, MODEL extends Serializable, TYPE extends Object> {

	Map<Object, List<MessageHandler<Object, Serializable, Object>>> MESSAGE_HANDLER = new ConcurrentSkipListMap<Object, List<MessageHandler<Object, Serializable, Object>>>();
	Comparator<MessageHandler<Object, Serializable, Object>> SORT = new Comparator<MessageHandler<Object, Serializable, Object>>() {
		@Override
		public int compare(final MessageHandler<Object, Serializable, Object> m1, final MessageHandler<Object, Serializable, Object> m2) {
			return m1.weight().compareTo(m2.weight());
		}
	};

	/***
	 * 添加处理器
	 * 
	 * @param handlers
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default MessageCenter<INPUT, MODEL, TYPE> addHandler(final MessageHandler<INPUT, MODEL, TYPE>... handlers) {
		if (CollectionUtil.newInstance().isEmpty((Object[]) handlers))
			return this;
		List<MessageHandler<Object, Serializable, Object>> list;
		for (MessageHandler<INPUT, MODEL, TYPE> item : handlers) {
			if (null == item)
				continue;
			list = MESSAGE_HANDLER.get(item.getType());
			list = null == list ? new LinkedList<MessageHandler<Object, Serializable, Object>>() : list;
			list.add((MessageHandler<Object, Serializable, Object>) item);
			list.sort(SORT);
		}
		return this;
	}

	/***
	 * 删除处理器
	 * 
	 * @param type
	 * @param clazz
	 */
	default void removeHandler(TYPE type, Class<? extends MessageHandler<?, ?, ?>> clazz) {
		if (null == type || null == clazz)
			return;
		List<MessageHandler<Object, Serializable, Object>> list = MESSAGE_HANDLER.get(type);
		if (null == list)
			return;
		Iterator<MessageHandler<Object, Serializable, Object>> it = list.iterator();
		while (it.hasNext()) {
			if (clazz.equals(it.getClass())) {
				it.remove();
				return;
			}
		}
	}

	/***
	 * 获取 处理集合
	 */
	@SuppressWarnings("unchecked")
	default List<MessageHandler<INPUT, MODEL, TYPE>> getHandler(TYPE type) {
		List<?> result = MESSAGE_HANDLER.get(type);
		return null == result ? null : (List<MessageHandler<INPUT, MODEL, TYPE>>) result;
	}

	/***
	 * 消息触发
	 * <P>
	 * 收到消息-->分析类型-->分派分流处理-->释放资源
	 * 
	 * @param input
	 */
	void doHandle(INPUT input);

}
