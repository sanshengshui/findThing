package com.sanshengshui.server.transport.custom;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class CustomTransportServerInitializer extends ChannelInitializer<SocketChannel>  {
    private final int maxPayloadSize;

    public CustomTransportServerInitializer(int maxPayloadSize){
        this.maxPayloadSize = maxPayloadSize;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        CustomTransportHandler handler = new CustomTransportHandler();

        pipeline.addLast(handler);
        ch.closeFuture().addListener(handler);

    }
}
