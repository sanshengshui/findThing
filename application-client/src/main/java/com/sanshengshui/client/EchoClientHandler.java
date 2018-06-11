package com.sanshengshui.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 穆书伟
 * @date 2018/6/11 下午13:05
 * @description 输出处理handler
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final static Logger LOGGER = LoggerFactory.getLogger(EchoClientHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                LOGGER.info("已经10秒没有发送消息！");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端和服务端建立连接时调用
        LOGGER.info("已经建立了联系..");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {
        //从服务端收到消息时被调用
        LOGGER.info("客户端收到消息={}",in.toString(CharsetUtil.UTF_8));
    }
}
