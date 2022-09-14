package com.example.demo.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * @author LTJ
 * @date 2022/7/22
 */
@Slf4j
public class ServerIOHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;
        while (request.readableBytes()>0){
            byte b = request.readByte();
            int b2=0xff & b;
            String s = Integer.toHexString(b2);
            System.out.print(s+"\t");
        }
        System.out.println();
        request.resetReaderIndex();
        log.info("服务器已收到消息：{}", request.toString(StandardCharsets.US_ASCII));
        log.info("服务器已收到消息：{}", request.toString(StandardCharsets.ISO_8859_1));
        log.info("服务器已收到消息：{}", request.toString(StandardCharsets.UTF_8));
        log.info("服务器已收到消息：{}", request.toString(StandardCharsets.UTF_16));
        log.info("服务器已收到消息：{}", request.toString(Charset.forName("GBK")));
        String rspMsg="当前时间："+new Date();
        ByteBuf response = Unpooled.wrappedBuffer(rspMsg.getBytes(Charset.forName("GBK")));
        ChannelFuture channelFuture = ctx.writeAndFlush(response);
//        channelFuture.addListener(ChannelFutureListener.CLOSE);
//        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
//        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("netty 接收消息异常", cause);
    }
}
