package com.safframework.server.converter.gson

import com.google.gson.Gson
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.server.converter.gson.GsonUtils
 * @author: Tony Shen
 * @date: 2020-03-25 00:18
 * @version: V1.0 <描述当前版本功能>
 */
object GsonUtils {

    private var gson: Gson = Gson()

    @JvmStatic
    fun <T> fromJson(json: String, type: Type): T = gson.fromJson(json, type)

    @JvmStatic
    fun <T> fromJson(jsonElement: JsonElement, type: Type): T = gson.fromJson(jsonElement, type)

    @JvmStatic
    fun toJson(data: Any): String = gson.toJson(data)
}