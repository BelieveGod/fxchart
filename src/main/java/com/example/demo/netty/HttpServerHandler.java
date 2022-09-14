package com.example.demo.netty;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author LTJ
 * @date 2022/8/3
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String format = StrUtil.format("receive http request,uri:{}, method:{}, content:{}", msg.uri(), msg.method(), msg.content().toString(CharsetUtil.UTF_8));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(format.getBytes(StandardCharsets.UTF_8))
        );
        ctx.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
