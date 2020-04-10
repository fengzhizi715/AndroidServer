package com.safframework.server.core

import com.safframework.server.core.http.Request
import com.safframework.server.core.http.Response
import com.safframework.server.core.http.controller.NotFoundController

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.TypeAliases
 * @author: Tony Shen
 * @date: 2020-03-21 17:23
 * @version: V1.0 <描述当前版本功能>
 */
typealias RequestHandler = (Request, Response) -> Response

typealias Consumer<T> = (T) -> Unit

typealias NotFound = NotFoundController