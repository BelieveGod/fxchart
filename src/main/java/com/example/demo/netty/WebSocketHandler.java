package com.example.demo.netty;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LTJ
 * @date 2022/8/3
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        String channelId = ctx.channel().id().asShortText();
        String format = StrUtil.format("channel:{} 接收到消息：{}", channelId, text);
        log.info(format);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(format);
        ChannelFuture channelFuture = ctx.writeAndFlush(textWebSocketFrame);
    }
}
