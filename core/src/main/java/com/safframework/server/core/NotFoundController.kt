package com.safframework.server.core

import com.safframework.androidserver.core.http.Request
import com.safframework.androidserver.core.http.Response
import org.json.JSONObject


/**
 *
 * @FileName:
 *          com.safframework.server.core.NotFoundController
 * @author: Tony Shen
 * @date: 2020-03-22 00:32
 * @version: V1.0 <描述当前版本功能>
 */
class NotFoundController: RequestHandler {

    override fun invoke(request: Request, response: Response): Response {
        val json = JSONObject()
        json.put("status", 404)
        json.put("reason", "404 Not Found")
        return response.setBodyJson(json)
    }
}