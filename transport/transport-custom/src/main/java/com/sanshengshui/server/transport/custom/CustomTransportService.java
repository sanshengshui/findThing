package com.sanshengshui.server.transport.custom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Service("CustomTransportService")
@ConditionalOnProperty(prefix = "custom", value = "enabled", havingValue = "true", matchIfMissing = false)
public class CustomTransportService {
    private static final Logger log = LoggerFactory.getLogger(CustomTransportService.class);
    @Value("${custom.bind_address}")
    private String host;
    @Value("${custom.bind_port}")
    private Integer port;

    @Value("${custom.netty.leak_detector_level}")
    private String leakDetectorLevel;
    @Value("${custom.netty.boss_group_thread_count}")
    private Integer bossGroupThreadCount;
    @Value("${custom.netty.worker_group_thread_count}")
    private Integer workerGroupThreadCount;
    @Value("${custom.netty.max_payload_size}")
    private Integer maxPayloadSize;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() throws Exception {
        log.info("Setting resource leak detector level to {}", leakDetectorLevel);
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(leakDetectorLevel.toUpperCase()));
        log.info("Starting Custom transport...");
        log.info("Starting Custom transport server");
        bossGroup = new NioEventLoopGroup(bossGroupThreadCount);
        workerGroup = new NioEventLoopGroup(workerGroupThreadCount);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new CustomTransportServerInitializer( maxPayloadSize));

        serverChannel = b.bind(host, port).sync().channel();
        log.info("Custom transport started!");
    }

    @PreDestroy
    public void shutdown()  {
        System.out.println("=====+=====");
        log.info("Stopping Custom transport!");
        try {
            serverChannel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        log.info("Custom transport stopped!");
    }
}
