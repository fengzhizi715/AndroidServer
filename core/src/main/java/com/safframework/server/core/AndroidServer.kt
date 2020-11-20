package com.safframework.server.core

import com.safframework.server.core.converter.Converter
import com.safframework.server.core.converter.ConverterManager
import com.safframework.server.core.handler.http.NettyHttpServerInitializer
import com.safframework.server.core.handler.socket.NettySocketServerInitializer
import com.safframework.server.core.handler.socket.SocketListener
import com.safframework.server.core.handler.websocket.NettyWebSocketServerInitializer
import com.safframework.server.core.http.HttpMethod
import com.safframework.server.core.http.filter.HttpFilter
import com.safframework.server.core.log.LogManager
import com.safframework.server.core.log.LogProxy
import com.safframework.server.core.router.RouteTable
import com.safframework.server.core.ssl.SslContextFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.ssl.SslContext
import java.net.UnknownHostException

/**
 *
 * @FileName:
 *          com.safframework.server.core.AndroidServer
 * @author: Tony Shen
 * @date: 2020-03-21 17:54
 * @version: V1.0 Server 的实现类
 */
class AndroidServer private constructor(private val builder: Builder) : Server {

    private var channelFuture: ChannelFuture? = null
    private val routeRegistry: RouteTable = RouteTable
    private var sslContext: SslContext? = null
    private var webSocketPath: String?=null
    private var listener: SocketListener<String>?=null
    private var bossGroupThread:Int = 0
    private var workerGroupThread:Int = 0
    private var wsOnly = false

    private lateinit var bossGroup: EventLoopGroup
    private lateinit var workerGroup: EventLoopGroup
    private lateinit var channelInitializer: ChannelInitializer<out SocketChannel>

    init {
        builder.errorController?.let { routeRegistry.errorController(it) } // 支持 RouteTable 中添加自定义的 errorController

        builder.logProxy?.let { LogManager.logProxy(it) }

        builder.converter?.let { ConverterManager.converter(it) }

        if (builder.useTls) {
            sslContext = SslContextFactory.createSslContext()
        }

        builder.bossGroupThread?.let {
            bossGroupThread = it
        }

        builder.workerGroupThread?.let {
            workerGroupThread = it
        }
    }

    override fun start() {
        // 如果 channelInitializer 已经赋值，则使用该值。 否则 AndroidServer 会自行判断提供哪种服务。
        // 一个 AndroidServer 实例只能提供一种服务，比如提供 Http 服务、提供 TCP 服务、提供 WebSocket 服务或同时提供 TCP/WebSocket 服务
        if (!isChannelInitializerInitialized()) {
            channelInitializer = when {
                routeRegistry.isNotEmpty() -> NettyHttpServerInitializer(routeRegistry, sslContext, builder)

                routeRegistry.isEmpty() && listener!=null -> {
                    if (wsOnly) {
                        NettyWebSocketServerInitializer(webSocketPath ?: "/ws", listener!!)
                    } else {
                        NettySocketServerInitializer(webSocketPath ?: "/ws", listener!!)
                    }
                }

                else -> {
                    LogManager.e(TAG, "channelInitializer is failed")
                    return
                }
            }
        }

        object : Thread() {
            override fun run() {

                val bootstrap = ServerBootstrap()
                bossGroup= NioEventLoopGroup(bossGroupThread)
                workerGroup= NioEventLoopGroup(workerGroupThread)
                try {
                    bootstrap
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel::class.java)
                        .localAddress(builder.address,builder.port)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childHandler(channelInitializer)
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
        }.start()
    }

    override fun request(method: HttpMethod, route: String, handler: RequestHandler): AndroidServer {
        routeRegistry.registHandler(method,route,handler)
        return this
    }

    override fun filter(route: String, httpFilter: HttpFilter): Server {
        routeRegistry.addFilter(route,httpFilter)
        return this
    }

    override fun socket(channelInitializer: ChannelInitializer<out SocketChannel>): Server {
        this.channelInitializer = channelInitializer
        return this
    }

    override fun websocket(webSocketPath: String, listener: SocketListener<String>): Server {
        this.webSocketPath = webSocketPath
        this.listener = listener
        this.wsOnly = true
        return this
    }

    override fun socketAndWS(webSocketPath: String?, listener: SocketListener<String>): AndroidServer {
        this.webSocketPath = webSocketPath
        this.listener = listener
        return this
    }

    private fun isWorkerGroupInitialized() = ::workerGroup.isInitialized

    private fun isBossGroupInitialized() = ::workerGroup.isInitialized

    private fun isChannelInitializerInitialized() = ::channelInitializer.isInitialized

    override fun close() {
        try {
            channelFuture?.channel()?.close()

            if(isWorkerGroupInitialized()) {
                workerGroup.shutdownGracefully()
            }

            if (isBossGroupInitialized()) {
                bossGroup.shutdownGracefully()
            }
        } catch (e: InterruptedException) {
            LogManager.e(TAG, e.message?:"error")
            throw RuntimeException(e)
        }
    }

    class Builder private constructor() {
        var port: Int = 8080
        var address: String = "0.0.0.0"
        var useTls: Boolean = false
        var maxContentLength: Int = 524228
        var errorController:RequestHandler?=null
        var logProxy: LogProxy?=null
        var converter: Converter?=null
        var bossGroupThread:Int = 0
        var workerGroupThread:Int = 0

        constructor(init: Builder.() -> Unit): this() { init() }

        /**
         * 设置服务的端口号
         */
        fun port(init: Builder.() -> Int) = apply { port = init() }

        /**
         * 设置服务的地址
         */
        fun address(init: Builder.() -> String) = apply { address = init() }

        /**
         * 是否支持 https
         */
        fun useTls(init: Builder.() -> Boolean) = apply { useTls = init() }

        fun maxContentLength(init: Builder.() -> Int) = apply { maxContentLength = init() }

        /**
         * 设置 http 服务失败的自定义控制器，只适用于 http 服务
         */
        fun errorController(init: Builder.() -> RequestHandler) = apply { errorController = init() }

        /**
         * 设置日志的实现类，便于开发者使用自己的日志框架
         */
        fun logProxy(init: Builder.()-> LogProxy) = apply { logProxy = init() }

        /**
         * 设置 converter 类
         */
        fun converter(init: Builder.()->Converter) = apply { converter = init() }

        /**
         * 设置 Android Server 的 bossGroup 线程数
         */
        fun bossGroupThread(init: Builder.() -> Int) = apply { bossGroupThread = init() }

        /**
         * 设置 Android Server 的 workerGroupThread 线程数
         */
        fun workerGroupThread(init: Builder.() -> Int) = apply { workerGroupThread = init() }

        fun build(): AndroidServer = AndroidServer(this)
    }

    companion object {
        private val TAG = "AndroidServer"
    }
}