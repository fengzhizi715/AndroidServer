package com.safframework.androidserver.core

import com.safframework.androidserver.core.http.HttpMethod
import com.safframework.androidserver.core.log.LogProxy
import io.netty.channel.ChannelFuture
import java.io.Closeable

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.AndroidServer
 * @author: Tony Shen
 * @date: 2020-03-21 17:54
 * @version: V1.0 <描述当前版本功能>
 */
class AndroidServer :HttpServer,Closeable {

    private var channelFuture: ChannelFuture? = null

    private var log: LogProxy? = null

    override fun start() {
    }

    override fun request(method: HttpMethod, route: String, handler: RequestHandler): HttpServer {

        return this
    }

    fun setLog(log: LogProxy) {
        this.log = log
    }

    override fun close() {
        try {
            channelFuture?.channel()?.close()

        } catch (e: InterruptedException) {
            log?.e("error", e.message?:"")
            throw RuntimeException(e)
        }
    }
}