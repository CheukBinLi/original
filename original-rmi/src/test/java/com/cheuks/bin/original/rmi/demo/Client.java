package com.cheuks.bin.original.rmi.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.net.packagedemo
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-03-07 21:55
 *
 *
 */
public class Client {

    private volatile ChannelHandlerContext channelHandlerContext;

    public void start(int port) throws InterruptedException {

        EventLoopGroup clientGroup = new NioEventLoopGroup(1);
        Bootstrap client = new Bootstrap();
        final CountDownLatch wait = new CountDownLatch(1);
        client
                .group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new MessageToMessageDecoder<ByteBuf>() {
//                            @Override
//                            protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
//
//                                int len = msg.readableBytes();
//                                byte[] buffer = new byte[len];
//                                msg.readBytes(buffer);
//
//                                out.add(new String(buffer));
//                            }
//                        });
                        ch.pipeline().addLast(new MessageToMessageEncoder<String>() {
                            @Override
                            protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
                                out.add(Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
                            }
                        });
//                        ch.pipeline().addLast(new FixedLengthFrameDecoder(5));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                            ByteBuf cache = null;

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                ByteBuf data = (ByteBuf) msg;
                                try {
                                    if (cache == null) {
                                        cache = ctx.alloc().buffer(1024);
                                    } else if (cache.writableBytes() > cache.maxCapacity() - data.readableBytes()) {
                                        ByteBuf newCache = ctx.alloc().compositeBuffer(cache.readableBytes() + data.readableBytes());
                                        newCache.writeBytes(cache);
                                        cache.release();
                                        cache = newCache;
                                    }
                                    //读取管道所有数据
                                    cache.writeBytes(data);
                                    //分包
                                                  List<ByteBuf> outByteBuf = new ArrayList<>();
//                                    List<String> outByteBuf = new ArrayList<>();
                                    int frameLength = 3;
                                    while (cache.readableBytes() >= frameLength) {
                                                      outByteBuf.add(cache.readBytes(frameLength));
//                                        outByteBuf.add(new String(cache.readBytes(frameLength).array()));
                                    }
                                    //丢弃半包
                                    if (cache.isReadable()) {
                                        cache.discardReadBytes();
                                    }
                                    //分包重新写入context
                                    for (Object tempBuf : outByteBuf) {
                                        ctx.fireChannelRead(tempBuf);
                                    }
                                } finally {
                                    data.release();
                                }
                            }
                        });
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                System.out.println("来自服务端消息:" + msg);
                            }

                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

                                try {
                                    super.channelRegistered(ctx);
                                    synchronized (this) {
                                        channelHandlerContext = ctx;
                                    }
                                } finally {
                                    wait.countDown();
                                }
                            }
                        });
                    }
                });
        client.connect("127.0.0.1", port).sync().channel();
        wait.await();
        channelHandlerContext.writeAndFlush("哈哈哈");
        channelHandlerContext.write("哈哈哈");
        channelHandlerContext.write("哈哈哈");
        channelHandlerContext.write("哈哈哈");
        channelHandlerContext.writeAndFlush("哈哈哈");

    }

    public static void main(String[] args) throws InterruptedException {
        new Client().start(10010);
    }

}
