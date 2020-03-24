package com.safframework.androidserver.core.http

import io.netty.channel.Channel
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.AsciiString

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.http.HttpResponse
 * @author: Tony Shen
 * @date: 2020-03-24 11:37
 * @version: V1.0 <描述当前版本功能>
 */
class HttpResponse(private val channel:Channel) : Response {

    override fun setStatus(status: HttpResponseStatus): Response {
        TODO("Not yet implemented")
    }

    override fun setBodyJson(serializeToJson: Any): Response {
        TODO("Not yet implemented")
    }

    override fun setBodyHtml(html: String): Response {
        TODO("Not yet implemented")
    }

    override fun setBodyData(contentType: String, data: ByteArray): Response {
        TODO("Not yet implemented")
    }

    override fun setBodyText(text: String): Response {
        TODO("Not yet implemented")
    }

    override fun addHeader(key: CharSequence, value: CharSequence): Response {
        TODO("Not yet implemented")
    }

    override fun addHeader(key: AsciiString, value: AsciiString): Response {
        TODO("Not yet implemented")
    }
}