package com.safframework.androidserver.core.handler.socket

import io.netty.channel.Channel

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.handler.socket.SocketListener
 * @author: Tony Shen
 * @date: 2020-03-26 23:20
 * @version: V1.0 <描述当前版本功能>
 */
interface SocketListener<T> {

    /**
     *
     * @param msg
     * @param ChannelId unique id
     */
    fun onMessageResponseServer(msg: T, ChannelId: String)

    /**
     * 与客户端建立连接
     *
     * @param channel
     */
    fun onChannelConnect(channel: Channel)

    /**
     * 与客户端断开连接
     * @param
     */
    fun onChannelDisConnect(channel: Channel)
}
