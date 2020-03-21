package com.safframework.androidserver.core.http

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.http.HttMethod
 * @author: Tony Shen
 * @date: 2020-03-21 12:33
 * @version: V1.0 <描述当前版本功能>
 */
enum class HttMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    OPTIONS;

    companion object {

        val allHttpMethod = arrayOf(
            GET,
            POST,
            HEAD,
            OPTIONS,
            DELETE,
            PUT
        )
    }
}