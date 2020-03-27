package com.safframework.server.core.http

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.HttpMethod
 * @author: Tony Shen
 * @date: 2020-03-21 12:33
 * @version: V1.0 <描述当前版本功能>
 */
enum class HttpMethod {

    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    TRACE,
    CONNECT,
    OPTIONS,
    PATCH;

    companion object {
        fun getMethod(method: io.netty.handler.codec.http.HttpMethod): HttpMethod {
            for (m in values()) {
                if (m.name == method.name()) {
                    return m
                }
            }
            return GET
        }
    }
}