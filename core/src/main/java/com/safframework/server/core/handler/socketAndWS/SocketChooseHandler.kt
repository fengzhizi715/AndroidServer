package com.safframework.server.core.handler.socketAndWS

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.socket.SocketChooseHandler
 * @author: Tony Shen
 * @date: 2020-03-25 13:46
 * @version: V1.0 <描述当前版本功能>
 */
class SocketChooseHandler(private val webSocketPath:String) : ByteToMessageDecoder() {

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: List<Any>) {
        val protocol = getBufStart(`in`)
        if (protocol.startsWith(WEBSOCKET_PREFIX)) {
            PipelineAdd.websocketAdd(ctx, webSocketPath)

            ctx.pipeline().remove("string_encoder")
            ctx.pipeline().remove("linebased")
            ctx.pipeline().remove("string_decoder")
        }
        `in`.resetReaderIndex()
        ctx.pipeline().remove(this.javaClass)
    }

    private fun getBufStart(`in`: ByteBuf): String {
        var length = `in`.readableBytes()
        if (length > MAX_LENGTH) {
            length =
                MAX_LENGTH
        }

        // 标记读位置
        `in`.markReaderIndex()
        val content = ByteArray(length)
        `in`.readBytes(content)
        return String(content)
    }

    companion object {
        /** 默认暗号长度为23  */
        private val MAX_LENGTH = 23
        /** WebSocket握手的协议前缀  */
        private val WEBSOCKET_PREFIX = "GET /"
    }
}
