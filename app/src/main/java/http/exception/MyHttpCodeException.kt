package http.exception

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 网络链接的 code码不正常 不是200
 */
class MyHttpCodeException : MyOkhttpException {
    constructor(codes: Int, msg: String?, exception: Exception? = null) : super(
        codes,
        msg,
        exception
    )
}