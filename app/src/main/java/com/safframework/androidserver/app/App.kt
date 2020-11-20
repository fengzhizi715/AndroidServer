package com.safframework.androidserver.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import kotlin.properties.Delegates

/**
 *
 * @FileName:
 *          com.safframework.androidserver.app.App
 * @author: Tony Shen
 * @date: 2020-11-20 23:59
 * @version: V1.0 <描述当前版本功能>
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }
}