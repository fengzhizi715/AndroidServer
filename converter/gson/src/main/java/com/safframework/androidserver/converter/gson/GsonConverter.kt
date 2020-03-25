package com.safframework.androidserver.converter.gson

import com.safframework.androidserver.converter.Converter
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.androidserver.converter.gson.GsonConverter
 * @author: Tony Shen
 * @date: 2020-03-25 00:19
 * @version: V1.0 <描述当前版本功能>
 */
class GsonConverter : Converter {

    override fun <T> fromJson(json: String, type: Type): T {

        return GsonUtils.fromJson(json,type)
    }

    override fun toJson(data: Any): String {

        return GsonUtils.toJson(data)
    }
}