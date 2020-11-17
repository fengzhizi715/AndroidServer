package com.safframework.server.core.log

/**
 *
 * @FileName:
 *          com.safframework.server.core.log.LogManager
 * @author: Tony Shen
 * @date: 2020-03-24 13:28
 * @version: V1.0 AndroidServer 内部使用的日志操作
 */
object LogManager {

    private var logProxy: LogProxy? = null

    fun logProxy(logProxy: LogProxy) {
        LogManager.logProxy = logProxy
    }

    fun e(tag:String , msg:String) {
        logProxy?.e(tag,msg)?: println(msg)
    }

    fun w(tag:String , msg:String) {
        logProxy?.w(tag,msg)?: println(msg)
    }

    fun i(tag:String , msg:String) {
        logProxy?.i(tag,msg)?: println(msg)
    }

    fun d(tag:String , msg:String) {
        logProxy?.d(tag,msg)?: println(msg)
    }
}