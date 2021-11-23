package com.gx.app.gappxutils.entiy

import androidx.annotation.Keep
import http.Pool

/**
 * Created by Android-小强 on 2021/5/24.
 * mailbox:980766134@qq.com
 * description: 后台返回数据集 父类
 */
@Keep
open class ReturnData<T> {
    fun isSuccess(): Boolean {
        return code == Pool.HTTP_RETURN_CODE_SUCCESS
    }

    /**
     * 返回码
     */
    @Keep
    var code: Int? = Pool.HTTP_RETURN_CODE_SUCCESS

    /**
     *  如果code 是 403 那么是封禁提示信息
     */
    @Keep
    var msg: String? = ""

    /**
     *
     */
    @Keep
    var status: String? = ""

    /**
     * 封禁id
     */
    @Keep
    var rid: String? = ""

    /**
     * 封禁 上报事件
     */
    @Keep
    var ename: String? = ""
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 封禁 上报事件 排重时间间隔
     */
    @Keep
    var m: Int? = 0
        get() {
            if (field == null) {
                return 0
            }
            return field
        }

    @Keep
    var data: T? = null
}