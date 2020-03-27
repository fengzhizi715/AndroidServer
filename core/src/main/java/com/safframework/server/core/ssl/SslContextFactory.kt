package com.safframework.server.core.ssl

import com.safframework.server.core.log.LogManager
import io.netty.handler.codec.http2.Http2SecurityUtil
import io.netty.handler.ssl.*
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.security.cert.CertificateException
import javax.net.ssl.SSLException

/**
 *
 * @FileName:
 *          com.safframework.server.core.ssl.SslContextFactory
 * @author: Tony Shen
 * @date: 2020-03-24 13:22
 * @version: V1.0 <描述当前版本功能>
 */
object SslContextFactory {

    fun createSslContext(): SslContext? {
         return try {
            val cert = SelfSignedCertificate()
            SslContextBuilder.forServer(cert.key(), cert.cert())
                .sslProvider(SslProvider.OPENSSL)
                /* NOTE: the cipher filter may not include all ciphers required by the HTTP/2 specification.
                 * Please refer to the HTTP/2 specification for cipher requirements.
                 */
                .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE)
                .applicationProtocolConfig(
                    ApplicationProtocolConfig(
                        ApplicationProtocolConfig.Protocol.ALPN,
                        ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                        ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                        ApplicationProtocolNames.HTTP_2,
                        ApplicationProtocolNames.HTTP_1_1)
                )
                .build()
        } catch (e: CertificateException) {
            LogManager.e("error", e.message?:"")
            throw RuntimeException(e)
        } catch (e: SSLException) {
             LogManager.e("error", e.message?:"")
            throw RuntimeException(e)
        }
    }
}