package com.gx.app.gappxutils;

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/11 14:14
 */

/**
 * 请求头 流 key
 */
const val HEADER_STREAM_KEY = "Content-Type"

/**
 * 请求头 流 value
 */
const val HEADER_STREAM_VALUE = "application/octet-stream"

/**
 * 请求头 x-app key
 */
const val HEADER_X_APP_KEY = "x-app"


/**
 * 请求头 X-Channel key
 */
const val HEADER_X_CHANNEL_KEY = "X-Channel"

class ParametricInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val build = request.newBuilder()
            .addHeader(HEADER_STREAM_KEY, HEADER_STREAM_VALUE)
            .addHeader(HEADER_X_APP_KEY, "com.gx.app.gappx.insidetest")
            .addHeader(HEADER_X_CHANNEL_KEY, "test")
            .build()
        return chain.proceed(build)
    }


}