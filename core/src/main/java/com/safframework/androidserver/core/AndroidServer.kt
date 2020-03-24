package com.safframework.androidserver.core

import com.safframework.androidserver.core.converter.Converter
import com.safframework.androidserver.core.http.HttpMethod
import com.safframework.androidserver.core.log.LogManager
import com.safframework.androidserver.core.log.LogProxy
import com.safframework.androidserver.core.router.RouteTable
import com.safframework.androidserver.core.ssl.SslContextFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.ssl.SslContext
import java.io.Closeable
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.UnknownHostException

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.AndroidServer
 * @author: Tony Shen
 * @date: 2020-03-21 17:54
 * @version: V1.0 <描述当前版本功能>
 */
class AndroidServer private constructor(private val builder: AndroidServer.Builder) :HttpServer,Closeable {

    private var channelFuture: ChannelFuture? = null
    private val routeRegistry: RouteTable = RouteTable
    private var sslContext: SslContext? = null

    init {
        builder.logProxy?.let {
            LogManager.logProxy(it)
        }

        if (builder.useTls) {
            sslContext = SslContextFactory.createSslContext()
        }
    }

    override fun start() {
        val bootstrap = ServerBootstrap()
        val bossEventLoopGroup = NioEventLoopGroup(1)
        val eventLoopGroup = NioEventLoopGroup()
        try {
            val address = InetAddress.getByName(builder.address)
            val socketAddress = InetSocketAddress(address, builder.port)
            bootstrap
                .group(bossEventLoopGroup, eventLoopGroup)
                .channel(NioServerSocketChannel::class.java)
                .localAddress(socketAddress)
                .childHandler(NettyServerInitializer(routeRegistry,sslContext,builder))
            val cf = bootstrap.bind()
            channelFuture = cf
            cf.sync()
            cf.channel().closeFuture().sync()
        } catch (e: UnknownHostException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            close()
        }
    }

    override fun request(method: HttpMethod, route: String, handler: RequestHandler): AndroidServer {

        routeRegistry.registHandler(method,route,handler)
        return this
    }

    override fun close() {
        try {
            channelFuture?.channel()?.close()

        } catch (e: InterruptedException) {
            LogManager.e("error", e.message?:"")
            throw RuntimeException(e)
        }
    }

    class Builder {
        var port: Int = 8080
        var address: String = "127.0.0.1"
        var useTls: Boolean = false
        var maxContentLength: Int = 524228
        var logProxy:LogProxy?=null
        var converter: Converter?=null

        fun port(port: Int):Builder {
            this.port = port
            return this
        }

        fun address(address: String):Builder {
            this.address = address
            return this
        }

        fun useTls(useTls: Boolean):Builder {
            this.useTls = useTls
            return this
        }

        fun maxContentLength(maxContentLength: Int):Builder {
            this.maxContentLength = maxContentLength
            return this
        }

        fun logProxy(logProxy: LogProxy):Builder {
            this.logProxy = logProxy
            return this
        }

        fun converter(converter: Converter):Builder {
            this.converter = converter
            return this
        }

        fun build(): AndroidServer {
            return AndroidServer(this)
        }
    }
}