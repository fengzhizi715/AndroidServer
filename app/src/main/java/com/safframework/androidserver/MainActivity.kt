package com.safframework.androidserver

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.safframework.androidserver.service.HttpService


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

        startService(Intent(this, HttpService::class.java))
    }
}