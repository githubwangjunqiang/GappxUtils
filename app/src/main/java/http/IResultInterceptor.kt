package http

import java.lang.Exception

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 自定义返回拦截器
 */
interface IResultInterceptor {
    /**
     * 拦截器 执行程序
     */
    fun intercept(
        url: String,
        startTime: Long,
        value: String? = null,
        exception: Exception? = null
    ): String?
}