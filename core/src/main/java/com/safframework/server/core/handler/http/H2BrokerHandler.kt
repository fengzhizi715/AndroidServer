package com.safframework.server.core.handler.http

import com.safframework.server.core.AndroidServer
import com.safframework.server.core.RequestHandler
import com.safframework.server.core.http.HttpMethod
import com.safframework.server.core.http.HttpRequest
import com.safframework.server.core.http.HttpResponse
import com.safframework.server.core.router.RouteTable
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http2.*
import io.netty.util.collection.IntObjectHashMap

/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.http.H2BrokerHandler
 * @author: Tony Shen
 * @date: 2020-04-07 18:57
 * @version: V1.0 <描述当前版本功能>
 */
internal class H2BrokerHandler(
    private val encoder: Http2ConnectionEncoder,
    private val routeRegistry: RouteTable,
    val builder: AndroidServer.Builder
) : Http2EventAdapter() {
    private val requestMap = IntObjectHashMap<HttpRequest>()
    private val handlerMap = IntObjectHashMap<RequestHandler>()
    private val maxPayloadBytes: Int = builder.maxContentLength

    init {
        encoder.connection().addListener(this)
    }

    override fun onHeadersRead(
        ctx: ChannelHandlerContext,
        streamId: Int,
        headers: Http2Headers,
        padding: Int,
        endOfStream: Boolean) {

        var contentLength: Long = 0
        try {
            contentLength = headers.getLong(CONTENT_LENGTH, 0)
        } catch (nfe: NumberFormatException) {
            // Malformed header, ignore.
            // This isn't supposed to happen, but does; see https://github.com/netty/netty/issues/7710 .
        }

        if (contentLength > maxPayloadBytes) {
            writeTooLargeResponse(ctx, streamId)
            return
        }

        var request: HttpRequest? = requestMap.get(streamId)
        var handler: RequestHandler = handlerMap.get(streamId)
        if (request == null) {
            // TODO:
//            request = HttpRequest.fromH2Headers(ctx.alloc(), headers)
            if (!endOfStream) requestMap[streamId] = request
        }

        if (handler == null) {
            handler = routeRegistry.getHandler(HttpMethod.getMethod(headers.method().toString()), headers.path().toString())
            if (!endOfStream && handler != null)
                handlerMap[streamId] = handler
        }

        // If there's no data expected, call the handler. Else, pass the handler and request through in
        // the context.
        if (endOfStream) {
            try {
                request?.let {
                    val response = handler.invoke(request, HttpResponse(ctx.channel())) as HttpResponse
                    writeResponse(ctx, streamId, response)
                }
            } catch (e: Exception) {
//                    log.error("route handler error", e)
                writeResponse(ctx, streamId, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.EMPTY_BUFFER)
            }
        }
    }

    override fun onHeadersRead(
        ctx: ChannelHandlerContext,
        streamId: Int,
        headers: Http2Headers,
        streamDependency: Int,
        weight: Short,
        exclusive: Boolean,
        padding: Int,
        endOfStream: Boolean) {
        // Ignore stream priority.
        onHeadersRead(ctx, streamId, headers, padding, endOfStream)
    }

    @Throws(Http2Exception::class)
    override fun onDataRead(ctx: ChannelHandlerContext,
                            streamId: Int,
                            data: ByteBuf,
                            padding: Int,
                            endOfStream: Boolean): Int {

        val request = requestMap.get(streamId)
        val totalRead = request.appendData(data)
        val processed = data.readableBytes() + padding
        if (totalRead > maxPayloadBytes) {
            writeTooLargeResponse(ctx, streamId)
            return processed
        }
        if (endOfStream) {
            val handler = handlerMap.get(streamId)

            try {
                val response = handler.invoke(request, HttpResponse(ctx.channel())) as HttpResponse
                writeResponse(ctx, streamId, response)
                return processed
            } catch (e: Exception) {
//                log.error("Error in handling Route", e)
                writeResponse(ctx, streamId, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.EMPTY_BUFFER)
            }

        }
        return super.onDataRead(ctx, streamId, data, padding, endOfStream)
    }

    private fun writeTooLargeResponse(ctx: ChannelHandlerContext?, streamId: Int) {
        ctx?.let {
            writeResponse(it, streamId, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER)
            it.flush()
            it.close()
        }
    }

    private fun writeResponse(ctx: ChannelHandlerContext,
                              streamId: Int,
                              status: HttpResponseStatus,
                              body: ByteBuf) {
        val headers = DefaultHttp2Headers(true)
        headers.setInt(CONTENT_LENGTH, body.readableBytes())
        headers.status(status.codeAsText())
        encoder.writeHeaders(ctx, streamId, headers, 0, false, ctx.newPromise())
        encoder.writeData(ctx, streamId, body, 0, true, ctx.newPromise())
    }

    private fun writeResponse(ctx: ChannelHandlerContext, streamId: Int, response: HttpResponse) {
        val headers = response.buildH2Headers()
        encoder.writeHeaders(ctx, streamId, headers, 0, false, ctx.newPromise())
        encoder.writeData(ctx, streamId, response.getBody(), 0, true, ctx.newPromise())
    }
}