package com.safframework.androidserver.core.router

/**
 * 字典树
 * @FileName:
 *          com.safframework.androidserver.core.router.PathTrie
 * @author: Tony Shen
 * @date: 2020-03-21 21:34
 * @version: V1.0 <描述当前版本功能>
 */
class PathTrie<T> @JvmOverloads constructor(
    separator: String = "/",
    wildcard: String = "*"
) {
    private var root: TrieNode
    private var separator: String
    private var wildcard: String

    init {
        this.separator = separator
        this.wildcard = wildcard
        this.root = TrieNode(separator, null, wildcard)
    }

    fun getRoot(): TrieNode {
        return root
    }

    fun setRoot(root: TrieNode) {
        this.root = root
    }

    fun getSeparator(): String {
        return separator
    }

    fun setSeparator(separator: String) {
        this.separator = separator
    }

    fun getWildcard(): String {
        return wildcard
    }

    fun setWildcard(wildcard: String) {
        this.wildcard = wildcard
    }

    fun insert(key: String, value: T) {
        val strings = split(key)
        if (strings.size == 0) {
            this.root.value = value
            return
        }
        this.root.insert(strings, 0, value)
    }

    @JvmOverloads
    fun fetch(
        key: String,
        params: MutableMap<String, String>? = null
    ): T? {
        val strings = split(key)
        return if (strings.size == 0) {
            root.value
        } else this.root.fetch(strings, 0, params)
    }

    private fun split(key: String): Array<String> {
        var tmp = key
        if (tmp.startsWith("/")) {
            tmp = tmp.substring(1)
        }
        return tmp.split("/").toTypedArray()
    }

    inner class TrieNode(
        var key: String,
        value: T?,
        wildcard: String
    ) {
        var value: T?
        private var children: MutableMap<String, TrieNode> = mutableMapOf()
        var pathVariableName: String? = null
        var isWildCard: Boolean

        init {
            this.value = value
            isWildCard = key == wildcard
            if (isPathVariable(key)) {
                pathVariableName = key.substring(key.indexOf('{') + 1, key.indexOf('}'))
            }
        }

        fun getChildren(): Map<String, TrieNode> {
            return children
        }

        fun setChildren(children: MutableMap<String, TrieNode>) {
            this.children = children
        }

        /**
         * POST /{index}/{type}/10001
         * @param path
         * @param index
         * @param value
         */
        fun insert(path: Array<String>, index: Int, value: T) {
            if (index >= path.size) {
                return
            }
            val endpoint = path[index]
            var key = endpoint
            if (isPathVariable(endpoint)) {
                key = wildcard
            }
            var node = children[key]
            //达到叶子节点
            if (node == null) {
                node = TrieNode(endpoint, if (index == path.size) null else value, wildcard)
                children[key] = node
                node.insert(path, index + 1, value)
            }
            node.insert(path, index + 1, value)
        }

        /**
         * GET /index/type/10001
         * @param path
         * @param index
         * @param params
         * @return
         */
        fun fetch(
            path: Array<String>,
            index: Int,
            params: MutableMap<String, String>?
        ): T? {
            var params = params
            if (index >= path.size) {
                return null
            }
            val endpoint = path[index]
            var node = children[endpoint]
            var isWildCard = false
            //获取path失败，再获取通配符
            if (node == null) {
                node = children[wildcard]
                if (node == null) {
                    return null
                }
                isWildCard = true
            }
            //设置path variable
            if (isPathVariable(node.key)) {
                if (params == null) {
                    params = mutableMapOf()
                }
                params[node.pathVariableName!!] = endpoint
            }
            return if (index == path.size - 1) {
                node.value
            } else node.fetch(path, index + 1, params)
        }

        fun isPathVariable(key: String): Boolean {
            return key.indexOf("{") != -1 && key.indexOf("}") != -1
        }

        override fun toString(): String {
            return "TrieNode{" +
                    "value=" + value +
                    ", key='" + key + '\'' +
                    ", children=" + children +
                    ", pathVariableName='" + pathVariableName + '\'' +
                    ", isWildCard=" + isWildCard +
                    '}'
        }
    }

    override fun toString(): String {
        return "PathTrie{" +
                "root=" + root +
                ", separator='" + separator + '\'' +
                ", wildcard='" + wildcard + '\'' +
                '}'
    }
}