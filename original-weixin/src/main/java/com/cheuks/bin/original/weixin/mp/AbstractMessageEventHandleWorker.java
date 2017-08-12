package com.cheuks.bin.original.weixin.mp;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.weixin.mp.MessageEventHandleWorker;
import com.cheuks.bin.original.common.weixin.mp.model.MessageEventModel;

/***
 * 
 * @Title: original-weixin
 * @Description: 消息处理线程
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月12日 上午9:56:23
 *
 */
public abstract class AbstractMessageEventHandleWorker implements MessageEventHandleWorker<Void> {

    private LinkedBlockingDeque<MessageEventModel> queue = new LinkedBlockingDeque<MessageEventModel>();

    protected final static Logger LOG = LoggerFactory.getLogger(AbstractMessageEventHandleWorker.class);

    private volatile boolean interrupt;

    public void interrupt() {
        interrupt = true;
        queue.clear();
    }

    public boolean isActivate() {
        return !interrupt;
    }

    public Void call() throws Exception {
        MessageEventModel messageEventModel;
        try {
            while (!interrupt) {
                synchronized (queue) {
                    if (queue.size() < 1) {
                        queue.wait();
                    }
                }
                messageEventModel = queue.pollFirst();
                if (null != messageEventModel) {
                    process(messageEventModel);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pushTask(List<MessageEventModel> messageEventModels) {
        if (null != messageEventModels && !messageEventModels.isEmpty()) {
            queue.addAll(messageEventModels);
            synchronized (queue) {
                queue.notify();
            }
        }
    }

    public void pushTask(MessageEventModel messageEventModels) {
        if (null != messageEventModels) {
            queue.add(messageEventModels);
            synchronized (queue) {
                queue.notify();
            }
        }
    }

    public int size() {
        return queue.size();
    }

}
