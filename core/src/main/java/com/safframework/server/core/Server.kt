package com.safframework.server.core

import com.safframework.server.core.handler.socketAndWS.SocketListener
import com.safframework.server.core.http.HttpMethod
import com.safframework.server.core.http.filter.HttpFilter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

/**
 * Server 接口，提供 http 方法、socket 方法
 * @FileName:
 *          com.safframework.server.core.HttpServer
 * @author: Tony Shen
 * @date: 2020-03-21 15:42
 * @version: V1.0  AndroidServer 的接口
 */
interface Server {

    @Throws(Exception::class)
    fun start()

    @Throws(Exception::class)
    fun close()

    /*** http 相关的方法 start ***/
    fun get(route: String, handler: RequestHandler): Server = request(HttpMethod.GET, route, handler)

    fun post(route: String, handler: RequestHandler): Server = request(HttpMethod.POST, route, handler)

    fun put(route: String, handler: RequestHandler): Server = request(HttpMethod.PUT, route, handler)

    fun delete(route: String, handler: RequestHandler): Server = request(HttpMethod.DELETE, route, handler)

    fun head(route: String, handler: RequestHandler): Server = request(HttpMethod.HEAD, route, handler)

    fun patch(route: String, handler: RequestHandler): Server = request(HttpMethod.PATCH, route, handler)

    fun trace(route: String, handler: RequestHandler): Server = request(HttpMethod.TRACE, route, handler)

    fun connect(route: String, handler: RequestHandler): Server = request(HttpMethod.CONNECT, route, handler)

    fun options(route: String, handler: RequestHandler): Server = request(HttpMethod.OPTIONS, route, handler)

    fun fileUpload(route: String, handler: RequestHandler): Server = request(HttpMethod.POST, route, handler)

    fun request(method: HttpMethod, route: String, handler: RequestHandler): Server

    fun filter(route:String, httpFilter: HttpFilter): Server
    /*** http 相关的方法 end ***/

    /**
     * 自定义的 TCP 服务
     */
    fun socket(channelInitializer: ChannelInitializer<out SocketChannel>): Server

    /**
     * 仅支持 WebSocket 服务
     */
    fun websocket(webSocketPath:String,listener: SocketListener<String>): Server

    /**
     * 一个端口同时支持 TCP/WebSocket 服务
     */
    fun socketAndWS(webSocketPath:String?,listener: SocketListener<String>): Server
}