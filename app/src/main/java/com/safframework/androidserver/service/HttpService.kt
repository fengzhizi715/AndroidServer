package com.safframework.androidserver.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.safframework.androidserver.log.LogImpl
import com.safframework.log.L
import com.safframework.server.converter.gson.GsonConverter
import com.safframework.server.core.AndroidServer
import com.safframework.server.core.http.Response
import java.io.File

/**
 *
 * @FileName:
 *          com.safframework.androidserver.service.HttpService
 * @author: Tony Shen
 * @date: 2020-03-27 10:31
 * @version: V1.0 <描述当前版本功能>
 */
class HttpService : Service() {

    private lateinit var androidServer: AndroidServer

    override fun onCreate() {
        super.onCreate()
        startServer()
    }

    // 启动 Http 服务端
    private fun startServer() {

        androidServer = AndroidServer.Builder{
            converter {
                GsonConverter()
            }
            logProxy {
                LogImpl
            }
        }.build()

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
                response.html(this,"test")
            }
            .fileUpload("/uploadFile") { request, response: Response -> // curl -v -F "file=@/Users/tony/1.png" 10.184.18.14:8080/uploadFile

                val uploadFile = request.file("file")
                val fileName = uploadFile.fileName
                val f = File("/sdcard/$fileName")
                val byteArray = uploadFile.content
                f.writeBytes(byteArray)

                response.setBodyText("upload success")
            }
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
