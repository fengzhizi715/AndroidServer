package com.safframework.server.core.http.filter

import com.safframework.server.core.http.Request
import com.safframework.server.core.http.Response

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.filter.HttpFilter
 * @author: Tony Shen
 * @date: 2020-05-17 12:45
 * @version: V1.0 <描述当前版本功能>
 */
interface HttpFilter {

    @Throws(Exception::class)
    fun before(request: Request, response: Response): Boolean

    @Throws(Exception::class)
    fun after(request: Request, response: Response)
}