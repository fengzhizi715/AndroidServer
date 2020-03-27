package com.safframework.server.core.http.session

import java.io.Serializable;

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.session.HttpSession
 * @author: Tony Shen
 * @date: 2020-03-21 15:05
 * @version: V1.0 <描述当前版本功能>
 */
interface HttpSession : Serializable {

    fun getAttr(name: String): Any

    fun setAttr(name: String, value: Any)

    fun getSessionId(): String

    fun setSessionId(sessionId: String)

    fun remove(name: String): Any

    fun clear()

    fun getMaxAge(): Int

    fun setMaxAge(maxAge: Int)

    fun isExpire(): Boolean

    fun getAll(): Map<String, Any>
}