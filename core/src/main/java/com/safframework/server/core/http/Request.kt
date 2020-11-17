package com.safframework.server.core.http

import com.safframework.server.core.http.cookie.HttpCookie
import com.safframework.server.core.http.entity.UploadFile

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.Request
 * @author: Tony Shen
 * @date: 2020-03-21 12:31
 * @version: V1.0 Request 请求
 */
interface Request {

    fun method(): HttpMethod

    fun url(): String

    fun headers(): MutableMap<String, String>

    fun header(name: String): String?

    fun cookies(): Set<HttpCookie>

    fun params(): MutableMap<String, String>

    fun param(name: String): String?

    fun content(): String

    fun file(name:String): UploadFile
}