package com.safframework.server.core.router

import com.safframework.server.core.NotFoundController
import com.safframework.server.core.RequestHandler
import com.safframework.androidserver.core.http.HttpMethod
import com.safframework.androidserver.core.http.Request

/**
 *
 * @FileName:
 *          com.safframework.server.core.router.RouteTable
 * @author: Tony Shen
 * @date: 2020-03-21 21:28
 * @version: V1.0 <描述当前版本功能>
 */
object RouteTable {

    private val getTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val postTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val putTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val deleteTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val headTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val traceTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val connectTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val optionsTrie: PathTrie<RequestHandler> =
        PathTrie()
    private val patchTrie: PathTrie<RequestHandler> =
        PathTrie()

    fun registHandler(method: HttpMethod, url: String, handler: RequestHandler) {
        getTable(method)
            .insert(url, handler)
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

    fun getHandler(request: Request): RequestHandler {

        return getTable(request.method())
            .fetch(request.url(),request.params())?: NotFoundController()
    }

    fun isNotEmpty():Boolean = getTrie.getRoot()!=null
            || postTrie.getRoot()!=null
            || putTrie.getRoot()!=null
            || deleteTrie.getRoot()!=null
            || headTrie.getRoot()!=null
            || traceTrie.getRoot()!=null
            || connectTrie.getRoot()!=null
            || optionsTrie.getRoot()!=null
            || patchTrie.getRoot()!=null

    fun isEmpty():Boolean = getTrie.getRoot()==null
            && postTrie.getRoot()==null
            && putTrie.getRoot()==null
            && deleteTrie.getRoot()==null
            && headTrie.getRoot()==null
            && traceTrie.getRoot()==null
            && connectTrie.getRoot()==null
            && optionsTrie.getRoot()==null
            && patchTrie.getRoot()==null
}