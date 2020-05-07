package com.safframework.server.core.http

import com.safframework.server.core.http.entity.UploadFile
import com.safframework.server.core.log.LogManager
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder
import io.netty.handler.codec.http.multipart.InterfaceHttpData
import io.netty.handler.codec.http.multipart.MemoryFileUpload


/**
 *
 * @FileName:
 *          com.safframework.server.core.http.ParamParser
 * @author: Tony Shen
 * @date: 2020-05-06 20:46
 * @version: V1.0 <描述当前版本功能>
 */
object ParamParser {

    fun parseFile(fullReq: FullHttpRequest, name: String): UploadFile {

        val uploadFile = UploadFile()
        val method: HttpMethod = HttpMethod.getMethod(fullReq.method())
        val headers: HttpHeaders = fullReq.headers()
        val contentType = headers[HttpHeaderNames.CONTENT_TYPE]
        if (HttpMethod.POST === method) {

            val decoder = HttpPostRequestDecoder(DefaultHttpDataFactory(false), fullReq)

            if (HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString() != contentType && !decoder.isMultipart) {
                return uploadFile
            }

            val paramlist = decoder.bodyHttpDatas

            for (param in paramlist) {
                if (param.httpDataType == InterfaceHttpData.HttpDataType.FileUpload) {
                    val data = param as MemoryFileUpload
                    if (data.name == name) {
                        uploadFile.content = data.get()
                        uploadFile.fileName = data.filename
                        uploadFile.contentType = data.contentType
                    }
                }
            }
        }
        return uploadFile
    }

}