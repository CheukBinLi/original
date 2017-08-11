package com.cheuks.bin.original.weixin.mp.handle.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.annotation.weixin.MessageEventHandleAnnotation;
import com.cheuks.bin.original.weixin.mp.MessageEventHandle;
import com.cheuks.bin.original.weixin.mp.model.MessageEventModel;

@MessageEventHandleAnnotation
public class TextMessageHandle implements MessageEventHandle {

    private static final Logger LOG = LoggerFactory.getLogger(TextMessageHandle.class);

    public String getMessageType() {
        return TEXT;
    }

    public void onMessage(MessageEventModel messageEventModel) {
        LOG.info("收到从openID:{},发送的[{}]消息：{}", messageEventModel.getFromUserName(), messageEventModel.getMsgType(), messageEventModel.getContent());
    }

}
