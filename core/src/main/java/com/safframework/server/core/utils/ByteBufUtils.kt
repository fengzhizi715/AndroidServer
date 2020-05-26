package com.safframework.server.core.utils

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled


/**
 *
 * @FileName:
 *          com.safframework.server.core.utils.ByteBufUtils
 * @author: Tony Shen
 * @date: 2020-05-26 21:03
 * @version: V1.0 <描述当前版本功能>
 */
fun toByteBuf(bytes: ByteArray?): ByteBuf {

    return if (bytes == null || bytes.isEmpty()) {
        Unpooled.EMPTY_BUFFER
    } else Unpooled.wrappedBuffer(bytes)
}