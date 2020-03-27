package com.safframework.androidserver.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.safframework.androidserver.log.LogProxy
import com.safframework.server.converter.gson.GsonConverter
import com.safframework.server.core.AndroidServer
import com.safframework.server.core.handler.socket.SocketListener
import com.safframework.server.core.log.LogManager
import io.netty.channel.Channel
import java.net.InetSocketAddress

/**
 *
 * @FileName:
 *          com.safframework.androidserver.service.SocketService
 * @author: Tony Shen
 * @date: 2020-03-27 17:01
 * @version: V1.0 <描述当前版本功能>
 */
class SocketService : Service() {

    private lateinit var androidServer: AndroidServer

    override fun onCreate() {
        super.onCreate()
        startServer()
    }

    // 启动 Socket 服务端
    private fun startServer() {
        androidServer = AndroidServer.Builder().converter(GsonConverter()).port(8888).logProxy(LogProxy).build()

        androidServer
            .socket("/ws", object: SocketListener<String> {
                override fun onMessageResponseServer(msg: String, ChannelId: String) {
                    LogManager.d("SocketService","msg = $msg")
                }

                override fun onChannelConnect(channel: Channel) {
                    val insocket = channel.remoteAddress() as InetSocketAddress
                    val clientIP = insocket.address.hostAddress
                    LogManager.d("SocketService","connect client: $clientIP")

                }

                override fun onChannelDisConnect(channel: Channel) {
                    val ip = channel.remoteAddress().toString()
                    LogManager.d("SocketService","disconnect client: $ip")
                }

            })
            .start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {

        androidServer.close()
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
