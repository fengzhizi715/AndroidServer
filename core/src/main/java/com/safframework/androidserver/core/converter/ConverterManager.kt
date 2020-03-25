package com.safframework.androidserver.core.converter

import com.safframework.androidserver.converter.Converter
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.converter.ConverterManager
 * @author: Tony Shen
 * @date: 2020-03-25 00:26
 * @version: V1.0 <描述当前版本功能>
 */
object ConverterManager {

    private var converter: Converter? = null

    fun converter(converter: Converter) {
        this.converter = converter
    }

    fun <T> fromJson(json: String, type: Type): T? = converter?.fromJson(json,type)

    fun toJson(data: Any): String? = converter?.toJson(data)
}