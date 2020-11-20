package com.safframework.androidserver.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.safframework.androidserver.log.LogProxyImpl
import com.safframework.server.converter.gson.GsonConverter
import com.safframework.server.core.AndroidServer
import com.safframework.server.core.handler.socket.SocketListener
import com.safframework.server.core.http.Request
import com.safframework.server.core.http.Response
import com.safframework.server.core.http.filter.HttpFilter
import com.safframework.server.core.log.LogManager
import io.netty.channel.Channel
import java.io.File
import java.net.InetSocketAddress

/**
 *
 * @FileName:
 *          com.safframework.androidserver.service.WebSocketService
 * @author: Tony Shen
 * @date: 2020-11-20 23:02
 * @version: V1.0 <描述当前版本功能>
 */
class WebSocketService : Service() {

    private lateinit var androidServer: AndroidServer

    override fun onCreate() {
        super.onCreate()
        startServer()
    }

    // 启动 WebSocket 服务端
    private fun startServer() {

        androidServer = AndroidServer.Builder{
            converter {
                GsonConverter()
            }
            logProxy {
                LogProxyImpl
            }
            port {
                8080
            }
        }.build()

        androidServer
            .websocket("/ws",object : SocketListener<String>{
                override fun onMessageResponseServer(msg: String, ChannelId: String) {
                    LogManager.d("WebSocketService","msg = $msg")
                }

                override fun onChannelConnect(channel: Channel) {
                    val insocket = channel.remoteAddress() as InetSocketAddress
                    val clientIP = insocket.address.hostAddress
                    LogManager.d("WebSocketService","connect client: $clientIP")

                }

                override fun onChannelDisConnect(channel: Channel) {
                    val ip = channel.remoteAddress().toString()
                    LogManager.d("WebSocketService","disconnect client: $ip")
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