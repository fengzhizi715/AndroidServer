package com.safframework.server.core.handler.http

import com.safframework.server.core.AndroidServer
import com.safframework.server.core.router.RouteTable
import io.netty.handler.codec.http2.*

/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.http.Http2HandlerBuilder
 * @author: Tony Shen
 * @date: 2020-04-07 18:47
 * @version: V1.0 <描述当前版本功能>
 */
internal class Http2HandlerBuilder(
    private val routeRegistry: RouteTable,
    private val builder: AndroidServer.Builder
) : AbstractHttp2ConnectionHandlerBuilder<Http2ConnectionHandler, Http2HandlerBuilder>() {

    public override fun build(): Http2ConnectionHandler {
        return super.build()
    }

    @Throws(Exception::class)
    override fun build(decoder: Http2ConnectionDecoder,
                       encoder: Http2ConnectionEncoder,
                       initialSettings: Http2Settings
    ): Http2ConnectionHandler {
        decoder.frameListener(H2BrokerHandler(encoder, routeRegistry, builder))
        return object : Http2ConnectionHandler(decoder, encoder, initialSettings) {
        }
    }
}