package com.safframework.server.core.http.entity

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.entity.UploadFile
 * @author: Tony Shen
 * @date: 2020-05-06 17:55
 * @version: V1.0 <描述当前版本功能>
 */
class UploadFile {

    var fileName:String?=null
    var path:String?=null
    var contentType:String?=null
    var content:ByteArray = ByteArray(0)
}