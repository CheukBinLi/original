package com.cheuks.bin.original.common.weixin.mp;

import com.cheuks.bin.original.common.weixin.mp.model.MessageEventModel;

public interface MessageEventHandleManager {

    void pushMessage(byte[] data) throws Throwable;

    void pushMessage(MessageEventModel message) throws Throwable;

    int getProcessors();

    void start(int processors);

    void start();

    void shutdown();

}
