package http.exception

import http.Pool

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 服务端返回空信息
 */
class MyEmptyException : MyOkhttpException {
    constructor(msg: String? = null, exception: Exception? = null) : super(
        Pool.HTTP_RETURN_CODE_ERROR,
        msg,
        exception
    )
}