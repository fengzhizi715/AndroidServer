package com.safframework.androidserver.core.http

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.AsciiString


/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.http.Response
 * @author: Tony Shen
 * @date: 2020-03-21 13:09
 * @version: V1.0 <描述当前版本功能>
 */
interface Response {

    fun setStatus(status: HttpResponseStatus): Response

    fun setBodyJson(serializeToJson: Any): Response

    fun setBodyHtml(html: String): Response

    fun setBodyData(contentType: String, data: ByteArray): Response

    fun setBodyText(text: String): Response

    fun addHeader(key: CharSequence, value: CharSequence): Response

    fun addHeader(key: AsciiString, value: AsciiString): Response
}