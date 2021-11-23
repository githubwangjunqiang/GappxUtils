package http.exception

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 自定义网络异常对象
 */
open class MyOkhttpException : Exception {

    open var code = 0
    open var msg: String? = ""
    open var exception: Exception? = null

    constructor(codes: Int, msg: String? = null, exception: Exception? = null) : super() {
        code = codes
        this.msg = msg
        this.exception = exception
    }

}