package http.exception

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 后台返回的code 码不正确
 */
class MyServiceCodeException : MyOkhttpException {
    constructor(code: Int, msg: String?, exception: Exception? = null) : super(
        code,
        msg,
        exception
    )
}