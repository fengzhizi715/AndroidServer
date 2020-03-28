package com.safframework.server.core.http.cookie

import io.netty.handler.codec.http.cookie.Cookie
import io.netty.handler.codec.http.cookie.DefaultCookie

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.cookie.HttpCookie
 * @author: Tony Shen
 * @date: 2020-03-21 12:54
 * @version: V1.0 <描述当前版本功能>
 */
class HttpCookie {

    private var cookie: Cookie

    constructor(name: String, value: String) {
        cookie = DefaultCookie(name, value)
    }

    constructor(cookie: Cookie) {
        this.cookie = cookie
        cookie.isHttpOnly = true
    }

    fun name(): String = cookie.name()

    fun value(): String = cookie.value()

    fun setValue(value: String) {
        cookie.setValue(value)
    }

    fun domain(): String = cookie.domain()

    fun setDomain(domain: String) {
        cookie.setDomain(domain)
    }

    fun path(): String = cookie.path()

    fun setPath(path: String) {
        cookie.setPath(path)
    }

    fun maxAge(): Long = cookie.maxAge()

    fun setMaxAge(maxAge: Long) {
        cookie.setMaxAge(maxAge)
    }

    fun isSecure(): Boolean = cookie.isSecure

    fun setSecure(secure: Boolean) {
        cookie.isSecure = secure
    }

    fun isHttpOnly(): Boolean = cookie.isHttpOnly

    fun setHttpOnly(httpOnly: Boolean) {
        cookie.isHttpOnly = httpOnly
    }

    fun get() = cookie

    operator fun compareTo(o: Cookie): Int = cookie.compareTo(o)

    override fun toString(): String = "HttpCookie{cookie=$cookie}"
}