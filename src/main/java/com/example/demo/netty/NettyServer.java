package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * @author LTJ
 * @date 2022/7/22
 */
@Component
@Slf4j
public class NettyServer implements CommandLineRunner {
    @PostConstruct
    private void init(){
        log.info("netty Server bean已生成");
    }

    public void start() throws Exception{
        final int port = 7777;

        // 创建eventLoopGroup
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建启动引导其
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("codec", new HttpServerCodec())
                                    .addLast("compressor", new HttpContentCompressor())
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    .addLast(new WebSocketServerProtocolHandler("/netty/ws", null, true, 65536 * 10))
                                    .addLast("websocketHandler", new WebSocketHandler());
//                                    .addLast("handler", new HttpServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = serverBootstrap.bind().sync();
            log.info("Http server started,Listening on port:{}",port);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture.runAsync(()->{
            try {
                start();
            } catch (Exception e) {

                log.error("",e);
            }
        });
    }
}
