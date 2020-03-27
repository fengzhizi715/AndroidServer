package com.safframework.server.core.handler.socket

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler

/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.socket.PipelineAdd
 * @author: Tony Shen
 * @date: 2020-03-25 13:48
 * @version: V1.0 <描述当前版本功能>
 */
object PipelineAdd {

    fun websocketAdd(ctx: ChannelHandlerContext, webSocketPath:String = "/ws") {

        println("PipelineAdd")
        // HttpServerCodec：将请求和应答消息解码为HTTP消息
        ctx.pipeline().addBefore("commonhandler", "http-codec", HttpServerCodec())

        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
        ctx.pipeline().addBefore("commonhandler", "aggregator", HttpObjectAggregator(65535))

        // ChunkedWriteHandler：向客户端发送HTML5文件,文件过大会将内存撑爆
        ctx.pipeline().addBefore("commonhandler", "http-chunked", ChunkedWriteHandler())

        ctx.pipeline().addBefore("commonhandler", "WebSocketAggregator", WebSocketFrameAggregator(65535))

        //用于处理websocket, /ws为访问websocket时的uri
        ctx.pipeline().addBefore("commonhandler", "ProtocolHandler", WebSocketServerProtocolHandler(webSocketPath))

    }
}