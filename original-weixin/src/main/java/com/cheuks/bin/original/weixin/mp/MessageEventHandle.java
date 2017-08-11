package com.cheuks.bin.original.weixin.mp;

import com.cheuks.bin.original.weixin.mp.contant.MessageType;
import com.cheuks.bin.original.weixin.mp.model.MessageEventModel;
import com.cheuks.bin.original.weixin.mp.model.message.BaseMessageResponse;

/***
 * 
 * @Title: original-weixin
 * @Description: 消息事件处理
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月11日 上午8:57:14
 *
 */
public interface MessageEventHandle extends MessageType {

    /***
     * handle类型:text,music,news等等
     * 
     * @return
     */
    String getMessageType();

    /***
     * 消息接收事件
     * 
     * @param messageEventModel
     */
    void onMessage(MessageEventModel messageEventModel);

}
