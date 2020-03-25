package com.safframework.androidserver.core.handler.tcp

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LineBasedFrameDecoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.util.CharsetUtil

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.handler.tcp.NettyTCPServerInitializer
 * @author: Tony Shen
 * @date: 2020-03-25 13:42
 * @version: V1.0 <描述当前版本功能>
 */
class NettyServerInitializer(private val webSocketPath:String) : ChannelInitializer<SocketChannel>() {

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {

        val pipeline = ch.pipeline()

        pipeline.addLast("active", ChannelActiveHandler())
        pipeline.addLast("socketChoose", SocketChooseHandler(webSocketPath))

        pipeline.addLast("string_encoder", StringEncoder(CharsetUtil.UTF_8))
        pipeline.addLast("linebased", LineBasedFrameDecoder(1024))
        pipeline.addLast("string_decoder", StringDecoder(CharsetUtil.UTF_8))
        pipeline.addLast("commonhandler", CustomerServerHandler())
    }
}