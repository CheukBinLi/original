package com.cheuks.bin.original.common.weixin.mp;

import java.util.List;
import java.util.concurrent.Callable;

import com.cheuks.bin.original.common.weixin.content.MessageType;
import com.cheuks.bin.original.common.weixin.mp.model.MessageEventModel;

public interface MessageEventHandleWorker<T> extends Callable<T>, MessageType {

    /***
     * 添加任务
     * 
     * @param messageEventModels
     */
    public void pushTask(List<MessageEventModel> messageEventModels);

    public void pushTask(MessageEventModel messageEventModels);

    /***
     * 待处理任务数量
     * 
     * @return
     */
    public int size();

    /***
     * 中断
     */
    public void interrupt();

    /***
     * 运行状态
     * 
     * @return
     */
    public boolean isActivate();

    /***
     * 处理
     * 
     * @param messageEventModel
     */
    public void process(MessageEventModel messageEventModel);

}
