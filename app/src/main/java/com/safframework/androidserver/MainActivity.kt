package com.safframework.androidserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.safframework.androidserver.log.LogProxyImpl
import com.safframework.androidserver.server.startHttpServer
import com.safframework.androidserver.server.startWebSocketServer
import com.safframework.kotlin.coroutines.runInBackground
import com.safframework.server.converter.gson.GsonConverter
import com.safframework.server.core.AndroidServer
import com.safframework.utils.localIPAddress
import kotlinx.android.synthetic.main.activity_main.*


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
    private val port = 8080

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initData()
    }

    private fun initData() {
        content.text = "内网IP：$localIPAddress \nAndroidServer库在${port}端口提供服务"

        runInBackground{ //  通过协程启动 AndroidServer
            androidServer = AndroidServer.Builder{
                converter {
                    GsonConverter()
                }
                logProxy {
                    LogProxyImpl
                }
                port {
                    port
                }
            }.build()

//            startWebSocketServer(androidServer)
            startHttpServer(this@MainActivity,androidServer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        androidServer.close()
    }
}