package com.safframework.androidserver.server

import android.content.Context
import com.safframework.server.core.AndroidServer
import com.safframework.server.core.handler.socketAndWS.SocketListener
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
 *          com.safframework.androidserver.server.Servers
 * @author: Tony Shen
 * @date: 2020-11-20 23:53
 * @version: V1.0 <描述当前版本功能>
 */
fun startHttpServer(context:Context, androidServer:AndroidServer) {

    androidServer
        .get("/hello") { _, response: Response ->
            response.setBodyText("hello world")
        }
        .get("/sayHi/{name}") { request, response: Response ->
            val name = request.param("name")
            response.setBodyText("hi $name!")
        }
        .post("/uploadLog") { request, response: Response ->
            val requestBody = request.content()
            response.setBodyText(requestBody)
        }
        .get("/downloadFile") { request, response: Response ->
            val fileName = "xxx.txt"
            File("/sdcard/$fileName").takeIf { it.exists() }?.let {
                response.sendFile(it.readBytes(),fileName,"application/octet-stream")
            }?: response.setBodyText("no file found")
        }
        .get("/test") { _, response: Response ->
            response.html(context,"test")
        }
        .fileUpload("/uploadFile") { request, response: Response -> // curl -v -F "file=@/Users/tony/1.png" 10.184.18.14:8080/uploadFile

            val uploadFile = request.file("file")
            val fileName = uploadFile.fileName
            val f = File("/sdcard/$fileName")
            val byteArray = uploadFile.content
            f.writeBytes(byteArray)

            response.setBodyText("upload success")
        }
        .filter("/sayHi/*", object : HttpFilter {
            override fun before(request: Request): Boolean {
                LogManager.d("HttpService","before....")
                return true
            }

            override fun after(request: Request, response: Response) {
                LogManager.d("HttpService","after....")
            }

        })
        .start()
}

fun startSocketServer(androidServer:AndroidServer) {
    androidServer
        .socketAndWS("/ws", object: SocketListener<String> {
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

fun startWebSocketServer(androidServer:AndroidServer) {
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