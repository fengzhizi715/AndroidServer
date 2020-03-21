package com.safframework.androidserver.core.http

import com.safframework.androidserver.core.http.cookie.HttpCookie

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.http.Request
 * @author: Tony Shen
 * @date: 2020-03-21 12:31
 * @version: V1.0 <描述当前版本功能>
 */
interface Request {

    fun method(): HttpMethod

    fun uri(): String

    fun headers(): Map<String, String>

    fun header(name: String): String

    fun cookies(): Array<HttpCookie>

    fun params(): Map<String, String>

    fun param(name: String): String

    fun content(): String
}