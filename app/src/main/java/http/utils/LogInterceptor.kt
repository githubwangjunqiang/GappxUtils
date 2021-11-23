package http.utils

import com.gx.app.gappxutils.utils.logd
import http.IResultInterceptor
import http.exception.MyOkhttpException
import http.Pool

/**
 * Created by Android-小强 on 2021/11/22.
 * mailbox:980766134@qq.com
 * description: 自定义网络拦截器
 */
class LogInterceptor : IResultInterceptor {

    override fun intercept(
        url: String,
        startTime: Long,
        value: String?,
        exception: Exception?
    ): String? {
        "${this.javaClass.canonicalName}-开始拦截".logd()
        val nanoTime = System.currentTimeMillis()
        val toMicros = nanoTime - startTime
        val stringBuilder = StringBuilder("\n请求连接：")
            .append(url)
            .append("\n")
            .append("startTime:")
            .append(startTime)
            .append("\n")
            .append("endTime:")
            .append(nanoTime)
            .append("\n")
            .append("duration:")
            .append(toMicros)
            .append("毫秒")
            .append("\n")

        when {
            exception is MyOkhttpException -> {
                stringBuilder.append("error-code:").append(exception.code)
                    .append("\n").append("error-msg:").append(exception.msg)
            }
            exception != null -> {
                stringBuilder.append("error-name:").append(exception.javaClass.canonicalName)
                    .append("\n").append("error-msg:").append(exception.localizedMessage)
            }
            value != null -> {
                stringBuilder.append("code:").append(Pool.HTTP_OK)
                    .append("\n").append("value:").append(value)
            }
        }
        stringBuilder.append("\nlog-end")
        stringBuilder.toString().logd()
        return value
    }
}