package com.sanshengshui.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Client {
    private final static Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private EventLoopGroup group = new NioEventLoopGroup();

    private SocketChannel channel;

    @Value("${client.bind_port}")
    private int port;

    @Value("${client.bind_address}")
    private String host;

    @PostConstruct
    public void start() throws InterruptedException {
        LOGGER.info("Starting Test client...");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());

        ChannelFuture future = bootstrap.connect(host,port).sync();
        if (future.isSuccess()) {
            LOGGER.info("Test client started!");
        }
        channel = (SocketChannel) future.channel();
    }

    @PreDestroy
    public void shutdown()  {
        LOGGER.info("Stopping Test client!");
        try {
            channel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        LOGGER.info("Test client stopped!");
    }



}
