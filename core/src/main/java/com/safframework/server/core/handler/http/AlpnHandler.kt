package com.safframework.server.core.handler.http

import com.safframework.server.core.AndroidServer
import com.safframework.server.core.Consumer
import com.safframework.server.core.log.LogManager
import com.safframework.server.core.router.RouteTable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.handler.ssl.ApplicationProtocolNames
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler

/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.http.AlpnHandler
 * @author: Tony Shen
 * @date: 2020-04-07 17:42
 * @version: V1.0 <描述当前版本功能>
 */
internal class AlpnHandler(
    private val consumer: Consumer<ChannelPipeline>,
    private val routeRegistry: RouteTable,
    private val builder: AndroidServer.Builder
) : ApplicationProtocolNegotiationHandler(ApplicationProtocolNames.HTTP_1_1) {

    @Throws(Exception::class)
    override fun configurePipeline(ctx: ChannelHandlerContext, protocol: String) {
        if (ApplicationProtocolNames.HTTP_2 == protocol) {
            LogManager.d("AlpnHandler","configuring pipeline for h2")
            ctx.pipeline().addLast(Http2HandlerBuilder(routeRegistry, builder).build())
            return
        }

        if (ApplicationProtocolNames.HTTP_1_1 == protocol) {
            LogManager.d("AlpnHandler","configuring pipeline for h1")
            consumer(ctx.pipeline())
            return
        }
        throw IllegalStateException("unknown protocol: $protocol")
    }
}