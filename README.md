# AndroidServer

基于 Kotlin+Netty 开发，为 Android 系统提供 Web Server 的服务，包括 Http、TCP、WebSocket

# Feature:

* 支持 Http、TCP、WebSocket 服务
* Http 的路由表采用字典树(Tried Tree)实现
* 日志隔离，开发者可以使用自己的日志库

# TODO List：

* 支持 HttpCookies、HttpSession
* 支持 Https
* 支持 HTTP/2
* 自定义拦截器
