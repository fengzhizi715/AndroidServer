package com.safframework.androidserver.core.http.cookie

import io.netty.handler.codec.http.cookie.Cookie
import io.netty.handler.codec.http.cookie.DefaultCookie

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.http.cookie.HttpCookie
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

    fun name(): String {
        return cookie.name()
    }

    fun value(): String {
        return cookie.value()
    }

    fun setValue(value: String) {
        cookie.setValue(value)
    }

    fun domain(): String {
        return cookie.domain()
    }

    fun setDomain(domain: String) {
        cookie.setDomain(domain)
    }

    fun path(): String {
        return cookie.path()
    }

    fun setPath(path: String) {
        cookie.setPath(path)
    }

    fun maxAge(): Long {
        return cookie.maxAge()
    }

    fun setMaxAge(maxAge: Long) {
        cookie.setMaxAge(maxAge)
    }

    fun isSecure(): Boolean {
        return cookie.isSecure
    }

    fun setSecure(secure: Boolean) {
        cookie.isSecure = secure
    }

    fun isHttpOnly(): Boolean {
        return cookie.isHttpOnly
    }

    fun setHttpOnly(httpOnly: Boolean) {
        cookie.isHttpOnly = httpOnly
    }

    operator fun compareTo(o: Cookie): Int {
        return cookie.compareTo(o)
    }

    fun getWrapper(): Cookie {
        return cookie
    }

    override fun toString(): String {
        return "HttpCookie{" +
                "cookie=" + cookie +
                '}'
    }
}