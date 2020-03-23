package com.safframework.androidserver.core.converter

import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.androidserver.core.converter.Converter
 * @author: Tony Shen
 * @date: 2020-03-23 20:51
 * @version: V1.0 <描述当前版本功能>
 */
interface Converter {

    /**
     * 将字符串转换成type类型的对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    fun <T> fromJson(json: String, type: Type): T

    /**
     * 将对象序列化成字符串对象
     * @param data
     * @return
     */
    fun toJson(data: Any): String
}