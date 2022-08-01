package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * @author LTJ
 * @date 2022/7/22
 */
//@Component
@Slf4j
public class NettyServer implements CommandLineRunner {
    @PostConstruct
    private void init(){
        log.info("netty Server bean已生成");
    }

    public void start() throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerIOHandler());
                    }
                });
        log.info("netty 正在启动 端口：{}", 7777);
        ChannelFuture channelFuture = serverBootstrap.bind(7777).sync();
        channelFuture.channel().closeFuture().sync();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
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
