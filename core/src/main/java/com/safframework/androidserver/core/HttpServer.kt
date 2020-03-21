package com.safframework.androidserver.core

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.HttpServer
 * @author: Tony Shen
 * @date: 2020-03-21 15:42
 * @version: V1.0 <描述当前版本功能>
 */
interface HttpServer {

    @Throws(Exception::class)
    fun start()

    @Throws(Exception::class)
    fun stop()
}