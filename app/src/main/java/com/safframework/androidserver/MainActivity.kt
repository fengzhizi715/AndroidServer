package com.safframework.androidserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.safframework.androidserver.log.LogProxyImpl
import com.safframework.androidserver.server.startWebSocketServer
import com.safframework.kotlin.coroutines.runInBackground
import com.safframework.server.converter.gson.GsonConverter
import com.safframework.server.core.AndroidServer


/**
 *
 * @FileName:
 *          com.safframework.androidserver.MainActivity
 * @author: Tony Shen
 * @date: 2020-03-24 20:31
 * @version: V1.0 <描述当前版本功能>
 */
class MainActivity : AppCompatActivity(){

    private lateinit var androidServer: AndroidServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runInBackground{
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

            startWebSocketServer(androidServer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        androidServer.close()
    }
}