package com.safframework.androidserver.core

import com.safframework.androidserver.core.http.Request
import com.safframework.androidserver.core.http.Response
import org.json.JSONObject


/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.NotFoundController
 * @author: Tony Shen
 * @date: 2020-03-22 00:32
 * @version: V1.0 <描述当前版本功能>
 */
class NotFoundController: RequestHandler {

    override fun invoke(request: Request, response: Response): Response {
        val json = JSONObject()
        val entity = JSONObject()
        entity.put("status", 404)
        entity.put("reason", "404 Not Found")
        json.put("error", entity)
        return response.setBodyJson(json)
    }
}