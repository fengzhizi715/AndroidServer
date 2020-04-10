package com.safframework.server.core.http

import com.safframework.server.core.converter.ConverterManager
import com.safframework.server.core.http.cookie.HttpCookie
import com.safframework.server.core.log.LogManager
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.cookie.ServerCookieEncoder
import io.netty.handler.codec.http2.DefaultHttp2Headers
import io.netty.handler.codec.http2.Http2Headers
import io.netty.util.AsciiString
import io.netty.util.CharsetUtil
import java.io.IOException
import java.io.OutputStream
import java.io.UnsupportedEncodingException

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.HttpResponse
 * @author: Tony Shen
 * @date: 2020-03-24 11:37
 * @version: V1.0 <描述当前版本功能>
 */
class HttpResponse(private val channel:Channel) : Response {

    private var status: HttpResponseStatus? = null
    private var body: ByteBuf? = null
    private var headers: MutableMap<AsciiString, AsciiString> = mutableMapOf()

    override fun setStatus(status: HttpResponseStatus): Response {
        this.status = status
        return this
    }

    override fun setBodyJson(any: Any): Response {
        val byteBuf = channel.alloc().directBuffer()
        try {
            ByteBufOutputStream(byteBuf).use { os: OutputStream ->
                ConverterManager.toJson(any)?.let { os.write(it.toByteArray()) }
                addHeader(HttpHeaderNames.CONTENT_TYPE, JSON)
                body = byteBuf
            }
        } catch (e: IOException) {
            LogManager.e("error serializing json", e.message?:"")
        }
        return this
    }

    override fun setBodyHtml(html: String): Response {
        val bytes = html.toByteArray(CharsetUtil.UTF_8)
        body = Unpooled.copiedBuffer(bytes)
        addHeader(HttpHeaderNames.CONTENT_TYPE, TEXT_HTML)
        return this
    }

    override fun setBodyData(contentType: String, data: ByteArray): Response {
        body = Unpooled.copiedBuffer(data)
        addHeader(HttpHeaderNames.CONTENT_TYPE, contentType)
        return this
    }

    override fun setBodyText(text: String): Response {
        val bytes = text.toByteArray(CharsetUtil.UTF_8)
        body = Unpooled.copiedBuffer(bytes)
        addHeader(HttpHeaderNames.CONTENT_TYPE, TEXT_PLAIN)
        return this
    }

    override fun sendFile(bytes: ByteArray , fileName: String , contentType: String): Response {
        var name: String = fileName
        try {
            name = String(fileName.toByteArray(), Charsets.ISO_8859_1)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        addHeader(HttpHeaderNames.CONTENT_DISPOSITION,ATTACHMENT + name)
        body = Unpooled.copiedBuffer(bytes)
        addHeader(HttpHeaderNames.CONTENT_TYPE, contentType)
        return this
    }

    override fun addHeader(key: CharSequence, value: CharSequence): Response = addHeader(AsciiString.of(key), AsciiString.of(value))

    override fun addHeader(key: AsciiString, value: AsciiString): Response {
        headers[key] = value
        return this
    }

    override fun addCookie(cookie: HttpCookie): Response {
        addHeader(SET_COOKIE, ServerCookieEncoder.LAX.encode(cookie.get()))
        return this
    }

    fun getBody(): ByteBuf = body ?: Unpooled.EMPTY_BUFFER

    private fun buildBodyData(): ByteBuf = body ?: Unpooled.EMPTY_BUFFER

    fun buildFullH1Response(): FullHttpResponse {
        var status = this.status
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status?:HttpResponseStatus.OK, buildBodyData())
        response.headers().set(HttpHeaderNames.SERVER, SERVER_VALUE)
        headers.forEach { (key, value) -> response.headers().set(key, value) }
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buildBodyData().readableBytes())
        return response
    }

    fun buildH2Headers(): Http2Headers {
        var status = this.status
        val http2Headers = DefaultHttp2Headers()
        http2Headers.status(status?.codeAsText()?:HttpResponseStatus.OK.codeAsText())
        http2Headers.set(HttpHeaderNames.SERVER, SERVER_VALUE)
        headers.forEach { (name, value) -> http2Headers.set(name, value) }
        http2Headers.setInt(HttpHeaderNames.CONTENT_LENGTH, buildBodyData().readableBytes())
        return http2Headers
    }

    companion object {
        private val SERVER_VALUE = AsciiString.of("monica") // 服务器的名称
        private val JSON = AsciiString.cached("application/json")
        private val TEXT_HTML = AsciiString.cached("text/html")
        private val TEXT_PLAIN = AsciiString.cached("text/plain")
        private val SET_COOKIE = AsciiString.cached("set-cookie")
        private val ATTACHMENT = "attachment;filename="
    }
}