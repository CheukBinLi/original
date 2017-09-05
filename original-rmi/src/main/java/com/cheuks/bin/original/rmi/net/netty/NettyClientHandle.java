package com.cheuks.bin.original.rmi.net.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.rmi.LoadBalanceFactory;
import com.cheuks.bin.original.common.rmi.model.TransmissionModel;
import com.cheuks.bin.original.common.rmi.net.MessageHandle;
import com.cheuks.bin.original.rmi.net.SimpleLdapFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandle extends NettyClientMessageHandleAdapter<NettyClientHandle, TransmissionModel> {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandle.class);

    private static final SimpleLdapFactory SIMPLE_LDAP_FACTORY = SimpleLdapFactory.instance();

    private volatile ChannelHandlerContext channelHandlerContext;

    private LoadBalanceFactory<String, Void> loadBalanceFactory;

    private volatile String id;

    public NettyClientHandle getObject() {
        return this;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransmissionModel msg) throws Exception {
        // System.out.println("收到消息_交给 invoke handler处理");
        if (MessageHandle.RMI_SERVICE_TYPE_RESPONSE == msg.getServiceType()) {
            setResult(msg);
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 对象池添加些对对象
        channelHandlerContext = ctx;
        NettyClient nettyClientObjectPool = ctx.channel().attr(NettyClient.NETTY_CLIENT_OBJECT_POOL).get();
        nettyClientObjectPool.addObject(this);
        String server = ctx.channel().remoteAddress().toString().replaceAll("/", "");
        try {
            //			id = registerClientHandler.register(server, server);
            id = loadBalanceFactory.getResourceAndUseRegistration();
        } catch (Throwable e) {
            LOG.error(null, e);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.ALL_IDLE)) {
            if (LOG.isDebugEnabled())
                LOG.debug("send hearBate package.");
            ctx.writeAndFlush(new TransmissionModel(RMI_SERVICE_TYPE_HEAR_BEAT));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        ctx.close();
        ctx.channel().close();
        super.channelInactive(ctx);
        LOG.warn("disconnect: reconnection");
        NettyClient nettyClientObjectPool = ctx.channel().attr(NettyClient.NETTY_CLIENT_OBJECT_POOL).get();
        nettyClientObjectPool.removeObject(this);
        //断线重连
        try {
            //			registerClientHandler.unRegister(id);
            loadBalanceFactory.cancleRegistration(id);
        } catch (Throwable e) {
            LOG.error(null, e);
        }
        try {
            nettyClientObjectPool.addConnection(id);
        } catch (Throwable e) {
            LOG.error(null, e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        ctx.channel().close();
    }

    public NettyClientHandle(LoadBalanceFactory<String, Void> loadBalanceFactory) {
        super();
        this.loadBalanceFactory = loadBalanceFactory;
    }

    public NettyClientHandle() {
        super();
    }

}
