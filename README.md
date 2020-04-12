# AndroidServer

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

基于 Kotlin + Netty 开发，为 Android 系统提供 Web Server 的功能，包括 Http、TCP、WebSocket

# Feature:

* 支持 Http、TCP、WebSocket 服务
* 支持 Rest 风格的 API
* Http 的路由表采用字典树(Tried Tree)实现
* 日志隔离，开发者可以使用自己的日志库
* core 模块只依赖 netty-all，不依赖其他第三方库

# 最新版本

模块|最新版本
---|:-------------:
android-server-core|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/android-server-core/images/download.svg) ](https://bintray.com/fengzhizi715/maven/android-server-core/_latestVersion)
android-server-converter-gson|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/android-server-converter-gson/images/download.svg) ](https://bintray.com/fengzhizi715/maven/android-server-converter-gson/_latestVersion)

# 下载安装

Gradle:

```groovy
implementation 'com.safframework.server:android-server-core:<latest-version>'
```

```groovy
implementation 'com.safframework.server:android-server-converter-gson:<latest-version>'
```

# Usage:

## Http 服务

通过使用 Service 来提供一个 http 服务，它的 http 服务本身支持 rest 风格、支持跨域、cookies 等。

```kotlin
class HttpService : Service() {

    private lateinit var androidServer: AndroidServer

    override fun onCreate() {
        super.onCreate()
        startServer()
    }

    // 启动 Http 服务端
    private fun startServer() {

        androidServer = AndroidServer.Builder{
            converter {
                GsonConverter()
            }
            logProxy {
                LogProxy
            }
        }.build()

        androidServer
            .get("/hello") { _, response: Response ->
                response.setBodyText("hello world")
            }
            .get("/sayHi/{name}") { request, response: Response ->
                val name = request.param("name")
                response.setBodyText("hi $name!")
            }
            .post("/uploadLog") { request, response: Response ->
                val requestBody = request.content()
                response.setBodyText(requestBody)
            }
            .get("/downloadFile") { request, response: Response ->
                val file = File("/sdcard/xxx.txt")

                file.readBytes()?.let {
                    response.sendFile(it,"test.txt","application/octet-stream")
                }
            }
            .start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        androidServer.close()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
```

测试：

```
curl -v 127.0.0.1:8080/hello
*   Trying 127.0.0.1...
* Connected to 127.0.0.1 (127.0.0.1) port 8080 (#0)
> GET /hello HTTP/1.1
> Host: 127.0.0.1:8080
> User-Agent: curl/7.50.1-DEV
> Accept: */*
>
< HTTP/1.1 200 OK
< server: monica
< content-type: text/plain
< content-length: 11
<
* Connection #0 to host 127.0.0.1 left intact
hello world
```

```
curl -v -d 测试 127.0.0.1:8080/uploadLog
*   Trying 127.0.0.1...
* Connected to 127.0.0.1 (127.0.0.1) port 8080 (#0)
> POST /uploadLog HTTP/1.1
> Host: 127.0.0.1:8080
> User-Agent: curl/7.50.1-DEV
> Accept: */*
> Content-Length: 6
> Content-Type: application/x-www-form-urlencoded
>
* upload completely sent off: 6 out of 6 bytes
< HTTP/1.1 200 OK
< server: monica
< content-type: text/plain
< content-length: 6
<
* Connection #0 to host 127.0.0.1 left intact
测试
```

## Socket 服务

Socket 服务，支持同一个端口同时提供 TCP/WebSocket 服务

```kotlin
class SocketService : Service() {

    private lateinit var androidServer: AndroidServer

    override fun onCreate() {
        super.onCreate()
        startServer()
    }

    // 启动 Socket 服务端
    private fun startServer() {

        androidServer = AndroidServer.Builder{
            port {
                8888
            }
            converter {
                GsonConverter()
            }
            logProxy {
                LogProxy
            }
        }.build()

        androidServer
            .socket("/ws", object: SocketListener<String> {
                override fun onMessageResponseServer(msg: String, ChannelId: String) {
                    LogManager.d("SocketService","msg = $msg")
                }

                override fun onChannelConnect(channel: Channel) {
                    val insocket = channel.remoteAddress() as InetSocketAddress
                    val clientIP = insocket.address.hostAddress
                    LogManager.d("SocketService","connect client: $clientIP")

                }

                override fun onChannelDisConnect(channel: Channel) {
                    val ip = channel.remoteAddress().toString()
                    LogManager.d("SocketService","disconnect client: $ip")
                }

            })
            .start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {

        androidServer.close()
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
```

Socket 服务可以使用 ：https://github.com/fengzhizi715/NetDiagnose 进行测试

# TODO List：

* 支持 HttpSession
* 支持 Https
* 支持 HTTP/2
* 自定义拦截器

联系方式
===

Wechat：fengzhizi715


> Java与Android技术栈：每周更新推送原创技术文章，欢迎扫描下方的公众号二维码并关注，期待与您的共同成长和进步。

![](https://github.com/fengzhizi715/NetDiscovery/blob/master/images/gzh.jpeg)


ChangeLog
===
[版本更新记录](CHANGELOG.md)

License
-------

    Copyright (C) 2017 - present, Tony Shen.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
