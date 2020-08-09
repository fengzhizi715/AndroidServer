package com.safframework.server.core.http

import android.content.Context
import com.safframework.server.core.http.cookie.HttpCookie
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.AsciiString


/**
 *
 * @FileName:
 *          com.safframework.server.core.http.Response
 * @author: Tony Shen
 * @date: 2020-03-21 13:09
 * @version: V1.0 <描述当前版本功能>
 */
interface Response {

    fun setStatus(code:Int): Response

    fun setStatus(status: HttpResponseStatus): Response

    fun setBodyJson(any: Any): Response

    fun setBodyHtml(html: String): Response

    fun setBodyData(contentType: String, data: ByteArray): Response

    fun setBodyText(text: String): Response

    fun sendFile(bytes: ByteArray , fileName: String , contentType: String): Response

    fun addHeader(key: CharSequence, value: CharSequence): Response

    fun addHeader(key: AsciiString, value: AsciiString): Response

    fun addCookie(cookie: HttpCookie): Response

    /**
     * 加载本地的 html 文件
     */
    fun html(context:Context, view: String): Response

    /**
     * 加载本地的 html 文件
     */
    fun html(context:Context, view: String, path: String): Response

    /**
     * 加载本地的 json 文件，便于调试时使用
     */
    fun json(context:Context, view: String): Response

    /**
     * 加载本地的 json 文件，便于调试时使用
     */
    fun json(context:Context, view: String, path: String): Response

    fun image(bytes: ByteArray): Response
}