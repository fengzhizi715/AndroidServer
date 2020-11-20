package com.safframework.server.core.handler.websocket

import com.safframework.server.core.handler.socket.SocketListener
import com.safframework.server.core.log.LogManager
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import java.net.InetSocketAddress


/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.websocket.TextWebSocketFrameHandler
 * @author: Tony Shen
 * @date: 2020-11-20 17:39
 * @version: V1.0 <描述当前版本功能>
 */
@ChannelHandler.Sharable
class TextWebSocketFrameHandler(private val mListener: SocketListener<String>) : SimpleChannelInboundHandler<TextWebSocketFrame>() {

    //读到客户端的内容并且向客户端去写内容
    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
        LogManager.d(TAG,"收到消息内容：${msg.text()}")
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {

        if (msg is TextWebSocketFrame) {
            mListener.onMessageResponseServer(msg.text() , ctx.channel().id().asShortText())
        }
    }

    // 断开连接
    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        LogManager.d(TAG, "channelInactive")

        val reAddr = ctx.channel().remoteAddress() as InetSocketAddress
        val clientIP = reAddr.address.hostAddress
        val clientPort = reAddr.port

        LogManager.d(TAG,"连接断开：$clientIP : $clientPort")

        mListener.onChannelDisConnect(ctx.channel())
    }

    companion object {

        private val TAG = "TextWebSocketFrameHandler"
    }
}