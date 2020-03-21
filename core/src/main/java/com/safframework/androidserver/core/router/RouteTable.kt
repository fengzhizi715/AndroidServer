package com.safframework.androidserver.core.router

import com.safframework.androidserver.core.NotFoundController
import com.safframework.androidserver.core.RequestHandler
import com.safframework.androidserver.core.http.HttpMethod
import com.safframework.androidserver.core.http.Request

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.router.RouteTable
 * @author: Tony Shen
 * @date: 2020-03-21 21:28
 * @version: V1.0 <描述当前版本功能>
 */
class RouteTable {

    private val getTrie: PathTrie<RequestHandler> = PathTrie()
    private val postTrie: PathTrie<RequestHandler> = PathTrie()
    private val putTrie: PathTrie<RequestHandler> = PathTrie()
    private val deleteTrie: PathTrie<RequestHandler> = PathTrie()

    fun registHandler(
        method: HttpMethod,
        url: String,
        handler: RequestHandler
    ) {
        getTable(method)?.let {
            it.insert(url, handler)
        }
    }

    private fun getTable(method: HttpMethod): PathTrie<RequestHandler>? {
        when (method) {
            HttpMethod.GET -> return getTrie
            HttpMethod.POST -> return postTrie
            HttpMethod.PUT -> return putTrie
            HttpMethod.DELETE -> return deleteTrie
        }
        return null
    }

    fun getHandler(request: Request): RequestHandler {
       return getTable(request.method())?.let {
            it.fetch(request.url(), request.params())
        }?: NotFoundController()
    }
}