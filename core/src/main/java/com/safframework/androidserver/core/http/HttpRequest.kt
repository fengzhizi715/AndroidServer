package com.safframework.androidserver.core.http

import com.safframework.androidserver.core.http.cookie.HttpCookie
import io.netty.handler.codec.http.FullHttpRequest


/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.http.HttpRequest
 * @author: Tony Shen
 * @date: 2020-03-23 20:15
 * @version: V1.0 <描述当前版本功能>
 */
class HttpRequest(private val fullHttpRequest: FullHttpRequest) :Request {

    private val params: MutableMap<String, String> = mutableMapOf()

    private val headers: MutableMap<String, String> = mutableMapOf()

    init {
        val list: List<Map.Entry<String, String>> = fullHttpRequest.headers().entries()
        for ((key, value) in list) {
            headers.put(key,value)
        }
    }

    override fun method(): HttpMethod = HttpMethod.getMethod(fullHttpRequest.method())

    override fun url(): String = fullHttpRequest.uri()

    override fun headers(): MutableMap<String, String>  = headers

    override fun header(name: String): String?  = headers[name]

    override fun cookies(): Array<HttpCookie> {
        TODO("Not yet implemented")
    }

    override fun params(): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun param(name: String): String {
        TODO("Not yet implemented")
    }
}