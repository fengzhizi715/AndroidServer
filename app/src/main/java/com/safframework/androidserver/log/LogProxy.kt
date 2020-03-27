package com.safframework.androidserver.log

import com.safframework.log.L
import com.safframework.server.core.log.LogProxy

/**
 *
 * @FileName:
 *          com.safframework.androidserver.log.LogProxy
 * @author: Tony Shen
 * @date: 2020-03-27 22:52
 * @version: V1.0 <描述当前版本功能>
 */
object LogProxy : LogProxy {

    override fun e(tag: String, msg: String) {
        L.e(tag,msg)
    }

    override fun w(tag: String, msg: String) {
        L.w(tag,msg)
    }

    override fun i(tag: String, msg: String) {
        L.i(tag,msg)
    }

    override fun d(tag: String, msg: String) {
        L.d(tag,msg)
    }

}