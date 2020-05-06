package com.safframework.server.core.http.entity

import java.io.File

/**
 *
 * @FileName:
 *          com.safframework.server.core.http.entity.FileParam
 * @author: Tony Shen
 * @date: 2020-05-06 17:51
 * @version: V1.0 <描述当前版本功能>
 */
data class FileParam(val paramName:String,val file:UploadFile)