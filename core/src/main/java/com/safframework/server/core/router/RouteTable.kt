package com.safframework.server.core.router

import com.safframework.server.core.NotFound
import com.safframework.server.core.RequestHandler
import com.safframework.server.core.http.HttpMethod
import com.safframework.server.core.http.Request

/**
 * Http 的路由表，http 的方法使用字典树存储
 * @FileName:
 *          com.safframework.server.core.router.RouteTable
 * @author: Tony Shen
 * @date: 2020-03-21 21:28
 * @version: V1.0 <描述当前版本功能>
 */
object RouteTable {

    private val getTrie: PathTrie<RequestHandler> = PathTrie()
    private val postTrie: PathTrie<RequestHandler> = PathTrie()
    private val putTrie: PathTrie<RequestHandler> = PathTrie()
    private val deleteTrie: PathTrie<RequestHandler> = PathTrie()
    private val headTrie: PathTrie<RequestHandler> = PathTrie()
    private val traceTrie: PathTrie<RequestHandler> = PathTrie()
    private val connectTrie: PathTrie<RequestHandler> = PathTrie()
    private val optionsTrie: PathTrie<RequestHandler> = PathTrie()
    private val patchTrie: PathTrie<RequestHandler> = PathTrie()
    private var errorController: RequestHandler?=null

    fun registHandler(method: HttpMethod, url: String, handler: RequestHandler) {
        getTable(method).insert(url, handler)
    }

    private fun getTable(method: HttpMethod): PathTrie<RequestHandler> =
        when (method) {
            HttpMethod.GET     -> getTrie
            HttpMethod.POST    -> postTrie
            HttpMethod.PUT     -> putTrie
            HttpMethod.DELETE  -> deleteTrie
            HttpMethod.HEAD    -> headTrie
            HttpMethod.TRACE   -> traceTrie
            HttpMethod.CONNECT -> connectTrie
            HttpMethod.OPTIONS -> optionsTrie
            HttpMethod.PATCH   -> patchTrie
        }

    /**
     * 支持自定义错误的
     */
    fun errorController(errorController: RequestHandler) {
        this.errorController = errorController
    }

    fun getHandler(request: Request): RequestHandler = getTable(request.method()).fetch(request.url(),request.params())
        ?: errorController
        ?: NotFound()

    fun getHandler(method: HttpMethod, path: String): RequestHandler = getTable(method).fetch(path)
        ?: errorController
        ?: NotFound()

    fun isNotEmpty():Boolean = !isEmpty()

    fun isEmpty():Boolean = getTrie.getRoot().getChildren().isEmpty()
            && postTrie.getRoot().getChildren().isEmpty()
            && putTrie.getRoot().getChildren().isEmpty()
            && deleteTrie.getRoot().getChildren().isEmpty()
            && headTrie.getRoot().getChildren().isEmpty()
            && traceTrie.getRoot().getChildren().isEmpty()
            && connectTrie.getRoot().getChildren().isEmpty()
            && optionsTrie.getRoot().getChildren().isEmpty()
            && patchTrie.getRoot().getChildren().isEmpty()
}