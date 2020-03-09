package com.cheuks.bin.original.rmi.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.anything.test.net.packagedemo
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-03-07 21:30
 *
 *
 */
public class Server {


    public void start(int port) throws InterruptedException {

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                                  @Override
                                  protected void initChannel(SocketChannel ch) throws Exception {
//                                      ch.pipeline().addLast(new MessageToMessageDecoder<ByteBuf>() {
//                                          @Override
//                                          protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
//
//                                              int len = msg.readableBytes();
//                                              byte[] buffer = new byte[len];
//                                              msg.readBytes(buffer);
//
//                                              out.add(new String(buffer));
//                                          }
//                                      });
                                      ch.pipeline().addLast(new MessageToMessageEncoder<String>() {
                                          @Override
                                          protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
                                              out.add(Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
                                          }
                                      });
//                                      ch.pipeline().addLast("frame", new FixedLengthFrameDecoder(5));
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
//                                                  List<String> outByteBuf = new ArrayList<>();
                                                  int frameLength = 3;
                                                  while (cache.readableBytes() >= frameLength) {
                                                      outByteBuf.add(cache.readBytes(frameLength));
//                                                      outByteBuf.add(new String(cache.readBytes(frameLength).array()));
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
                                          public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                              super.channelActive(ctx);
                                              ctx.writeAndFlush(Unpooled.copiedBuffer("注册成功。".getBytes("UTF-8")));
                                          }

                                          @Override
                                          protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                              byte[] content = new byte[msg.readableBytes()];
                                              msg.readBytes(content);
                                              System.out.println("收到客户端消息：" + new String(content));
                                              ReferenceCountUtil.release(msg);
                                          }
                                      });
                                  }
                              }
                );
        server.bind(port).sync().channel().closeFuture();
    }

    public static void main(String[] args) throws InterruptedException {
        new Server().start(10010);
    }

}
