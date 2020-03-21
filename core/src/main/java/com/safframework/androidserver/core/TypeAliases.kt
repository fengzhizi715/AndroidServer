package com.safframework.androidserver.core

import com.safframework.androidserver.core.http.Request
import com.safframework.androidserver.core.http.Response


/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.TypeAliases
 * @author: Tony Shen
 * @date: 2020-03-21 17:23
 * @version: V1.0 <描述当前版本功能>
 */
typealias RequestHandler = (Request, Response) -> Response