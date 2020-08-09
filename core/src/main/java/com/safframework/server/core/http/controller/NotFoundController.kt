package com.safframework.server.core.http.controller

import com.safframework.server.core.RequestHandler
import com.safframework.server.core.http.Request
import com.safframework.server.core.http.Response
import org.json.JSONObject


/**
 * @FileName:
 *          com.safframework.server.core.http.controller.NotFoundController
 * @author: Tony Shen
 * @date: 2020-03-22 00:32
 * @version: V1.0 找不到路径时，默认的 RequestHandler
 */
class NotFoundController: RequestHandler {

    override fun invoke(request: Request, response: Response): Response {

        return response.setBodyJson(JSONObject().apply {
            put("status", 404)
            put("reason", "404 Not Found")
        })
    }
}