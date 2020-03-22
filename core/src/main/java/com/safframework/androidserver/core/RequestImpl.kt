package com.safframework.androidserver.core

import com.safframework.androidserver.core.http.HttpMethod
import com.safframework.androidserver.core.http.Request
import com.safframework.androidserver.core.http.cookie.HttpCookie

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.RequestImpl
 * @author: Tony Shen
 * @date: 2020-03-22 16:43
 * @version: V1.0 <描述当前版本功能>
 */
class RequestImpl:Request {

    override fun method(): HttpMethod {
        TODO("Not yet implemented")
    }

    override fun url(): String {
        TODO("Not yet implemented")
    }

    override fun headers(): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun header(name: String): String {
        TODO("Not yet implemented")
    }

    override fun cookies(): Array<HttpCookie> {
        TODO("Not yet implemented")
    }

    override fun params(): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun param(name: String): String {
        TODO("Not yet implemented")
    }

    override fun content(): String {
        TODO("Not yet implemented")
    }
}