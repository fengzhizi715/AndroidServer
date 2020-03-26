package com.safframework.androidserver.core.handler.socket

import com.safframework.androidserver.core.log.LogManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import io.netty.util.CharsetUtil
import java.net.InetSocketAddress

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.handler.socket.CustomerServerHandler
 * @author: Tony Shen
 * @date: 2020-03-25 13:48
 * @version: V1.0 <描述当前版本功能>
 */
@ChannelHandler.Sharable
class CustomerServerHandler(private val mListener: SocketListener<String>) : SimpleChannelInboundHandler<Any>() {

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext,
                                 cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {

        val buff = msg as ByteBuf
        val info = buff.toString(CharsetUtil.UTF_8)
        LogManager.d(TAG,"收到消息内容：$info")
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {

        if (msg is WebSocketFrame) {  // WebSocket消息处理

            val webSocketInfo = (msg as TextWebSocketFrame).text().trim { it <= ' ' }

            LogManager.d(TAG, "收到WebSocketSocket消息：$webSocketInfo")

            mListener.onMessageResponseServer(webSocketInfo , ctx.channel().id().asShortText())
        } else if (msg is String){   // Socket消息处理

            LogManager.d(TAG, "收到socket消息：$msg")

            mListener.onMessageResponseServer(msg, ctx.channel().id().asShortText())
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

        private val TAG = "CustomerServerHandler"
    }
}