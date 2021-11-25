package http.utils

/**
 * Created by Android-小强 on 2021/11/24.
 * mailbox:980766134@qq.com
 * description: 请求常量
 */
object CallPool {
    /**
     * 请求头 流 value
     */
    const val HEADER_STREAM_VALUE = "application/octet-stream"

    /**
     * 请求通用状态码 http 200
     */
    const val HTTP_OK = 200

    /**
     * 返回码的 key
     */
    const val HTTP_RETURN_CODE = "code"

    /**
     * 返回信息的 key
     */
    const val HTTP_RETURN_MSG = "msg"

    /**
     * 返回码的 key 成功值
     */
    const val HTTP_RETURN_CODE_SUCCESS = 0

    /**
     * 返回失败
     */
    const val HTTP_RETURN_CODE_ERROR = -1
}