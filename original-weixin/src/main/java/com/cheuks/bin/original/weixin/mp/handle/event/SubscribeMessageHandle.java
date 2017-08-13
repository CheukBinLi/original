package com.cheuks.bin.original.weixin.mp.handle.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.annotation.weixin.MessageEventHandleAnnotation;
import com.cheuks.bin.original.common.weixin.mp.MessageEventHandle;
import com.cheuks.bin.original.common.weixin.mp.model.MessageEventModel;

@MessageEventHandleAnnotation
public class SubscribeMessageHandle implements MessageEventHandle {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeMessageHandle.class);

    public String getMessageType() {
        return EVENT_SUBSCRIBE;
    }

    public void onMessage(MessageEventModel messageEventModel) {
        LOG.info("收到从openID:{},发送的[{}]EventKey：{},Ticket:{}", messageEventModel.getFromUserName(), messageEventModel.getMsgType(), messageEventModel.getEventKey(), messageEventModel.getTicket());
    }

}
