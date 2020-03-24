package com.safframework.androidserver.core

import com.safframework.androidserver.core.router.RouteTable
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.ssl.SslContext

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.NettyServerInitializer
 * @author: Tony Shen
 * @date: 2020-03-22 16:30
 * @version: V1.0 <描述当前版本功能>
 */
class NettyServerInitializer(routeRegistry: RouteTable,sslContext: SslContext?) : ChannelInitializer<SocketChannel>() {

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {

        val pipeline = ch.pipeline()

    }
}