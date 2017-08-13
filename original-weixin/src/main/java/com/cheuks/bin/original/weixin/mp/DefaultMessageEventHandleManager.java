package com.cheuks.bin.original.weixin.mp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cheuks.bin.original.common.annotation.weixin.MessageEventHandleAnnotation;
import com.cheuks.bin.original.common.util.XmlReaderAll;
import com.cheuks.bin.original.common.weixin.mp.MessageEventHandle;
import com.cheuks.bin.original.common.weixin.mp.MessageEventHandleManager;
import com.cheuks.bin.original.common.weixin.mp.MessageEventHandleWorker;
import com.cheuks.bin.original.common.weixin.mp.model.MessageEventModel;

public class DefaultMessageEventHandleManager implements ApplicationContextAware, MessageEventHandleManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMessageEventHandleManager.class);

    private final Map<String, MessageEventHandle> messageEventHandleManager = new ConcurrentSkipListMap<String, MessageEventHandle>();

    private List<MessageEventModel> messageQueue = new CopyOnWriteArrayList<MessageEventModel>();

    private List<MessageEventHandleWorker<?>> messageEventHandleWorkers = new ArrayList<MessageEventHandleWorker<?>>();

    private XmlReaderAll xmlReaderAll = XmlReaderAll.newInstance();

    /** 处理单批最大数量 */
    private int batchCount = 5;

    /** 最大并发处理任务数 */
    private int handleMaxTask = 100;

    /** 线程数 */
    private int processors = ((this.processors = Runtime.getRuntime().availableProcessors()) > 4 ? this.processors / 2 : this.processors);

    /** 是否在运行 */
    private volatile boolean isStart = false;

    /** 中断标记 */
    private volatile boolean interrupt = false;

    /** 线程池 */
    private ExecutorService executorService;

    public MessageEventModel pushMessage(byte[] data) throws Throwable {
        MessageEventModel result = xmlReaderAll.padding(data, new MessageEventModel());
        try {
            return result;
        } finally {
            pushMessage(result);
        }
    }

    public void pushMessage(MessageEventModel message) throws Throwable {
        messageQueue.add(message);
        synchronized (messageQueue) {
            messageQueue.notify();
        }
    }

    public int getProcessors() {
        return this.processors;
    }

    public void start(int processors) {
        this.processors = processors;
        start();
    }

    public void start() {
        if (isStart)
            return;
        interrupt = false;
        executorService = Executors.newFixedThreadPool(processors + 1);
        MessageEventHandleWorker<?> eventHandleWorker = new AbstractMessageEventHandleWorker() {

            public void process(MessageEventModel messageEventModel) {
                String msgType = EVENT.equals(msgType = messageEventModel.getMsgType()) ? messageEventModel.getEvent() : msgType;
                //处理
                MessageEventHandle messageEventHandle = messageEventHandleManager.get(msgType);
                if (null != messageEventHandle) {
                    messageEventHandle.onMessage(messageEventModel);
                } else {
                    LOG.info("收到[{}]类型消息，没找到些类默认 MessageEventHandle。", messageEventModel.getMsgType());
                }
            }
        };
        messageEventHandleWorkers.add(eventHandleWorker);
        for (int i = 0; i < processors; i++) {
            executorService.submit(eventHandleWorker);
        }

        //消息分发
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    int size;
                    List<MessageEventModel> tempList;
                    while (!interrupt) {
                        for (MessageEventHandleWorker<?> worker : messageEventHandleWorkers) {
                            if (interrupt)
                                return;
                            synchronized (messageQueue) {
                                if (messageQueue.size() < 1) {
                                    messageQueue.wait();
                                }
                            }
                            if ((size = messageQueue.size()) > 0 && worker.size() < handleMaxTask) {
                                if (size == 1) {
                                    worker.pushTask(messageQueue.remove(0));
                                } else if (messageQueue.size() > batchCount) {
                                    worker.pushTask(tempList = messageQueue.subList(0, batchCount));
                                    tempList.clear();
                                } else {
                                    worker.pushTask(tempList = messageQueue.subList(0, size));
                                    tempList.clear();
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    LOG.error(null, e);
                }
            }
        });
    }

    public void shutdown() {
        if (!isStart)
            return;
        interrupt = true;
        for (MessageEventHandleWorker<?> work : messageEventHandleWorkers) {
            work.interrupt();
        }
        executorService.shutdownNow();
        isStart = false;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MessageEventHandle messageEventHandle;
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MessageEventHandleAnnotation.class);
        if (null != beans) {
            for (Entry<String, Object> en : beans.entrySet()) {
                messageEventHandle = (MessageEventHandle) en.getValue();
                messageEventHandleManager.put(messageEventHandle.getMessageType(), messageEventHandle);
            }
        }
    }

}
