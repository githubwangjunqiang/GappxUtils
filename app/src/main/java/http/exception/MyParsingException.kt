package http.exception

import http.Pool

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 自定义解析出现异常
 */
class MyParsingException : MyOkhttpException {
    constructor(exception: Exception? = null) : super(Pool.HTTP_RETURN_CODE_ERROR, null, exception)
}