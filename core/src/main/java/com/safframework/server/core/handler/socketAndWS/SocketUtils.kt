package com.safframework.server.core.handler.socketAndWS

import com.safframework.server.core.log.LogManager
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
 *
 * @FileName:
 *          com.safframework.server.core.handler.socket.SocketUtils
 * @author: Tony Shen
 * @date: 2020-11-20 11:54
 * @version: V1.0 <描述当前版本功能>
 */
private val TAG = "SocketUtils"

/**
 * 异步发送TCP消息
 */
fun sendMsgToClient(data: String, channel:Channel?, listener: ChannelFutureListener) = channel?.run {
    val flag = this.isActive

    if (flag) {
        this.writeAndFlush(data + System.getProperty("line.separator")).addListener(listener)
    }

    flag
} ?: run {
    LogManager.d(TAG,"channel is null")
    false
}

/**
 * 同步发送TCP消息
 */
fun sendMsgToClient(data: String, channel:Channel?) = channel?.run {

    if (this.isActive) {
        return this.writeAndFlush(data + System.getProperty("line.separator")).awaitUninterruptibly().isSuccess
    }

    false
} ?: run {
    LogManager.d(TAG,"channel is null")
    false
}

/**
 * 异步发送 WebSocket 消息
 */
fun sendMsgToWS(data: String, channel:Channel?, listener: ChannelFutureListener) = channel?.run {

    val flag = this.isActive

    if (flag) {
        this.writeAndFlush(TextWebSocketFrame(data)).addListener(listener)
    }

    flag
} ?: run {
    LogManager.d(TAG,"channel is null")
    false
}

/**
 * 同步发送 WebSocket 消息
 */
fun sendMsgToWS(data: String,channel:Channel?) = channel?.run {

    if (this.isActive) {
        return this.writeAndFlush(TextWebSocketFrame(data)).awaitUninterruptibly().isSuccess
    }

    false
} ?: run {
    LogManager.d(TAG,"channel is null")
    false
}