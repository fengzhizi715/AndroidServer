package com.safframework.androidserver

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.safframework.androidserver.core.AndroidServer
import com.safframework.androidserver.core.http.Response

/**
 *
 * @FileName:
 *          com.safframework.androidserver.MainActivity
 * @author: Tony Shen
 * @date: 2020-03-24 20:31
 * @version: V1.0 <描述当前版本功能>
 */
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().post {
            val androidServer = AndroidServer.Builder().build()

            androidServer
                .get("/hello")  { _, response: Response ->
                    response.setBodyText("hello world")
                }
                .start()
        }
    }
}