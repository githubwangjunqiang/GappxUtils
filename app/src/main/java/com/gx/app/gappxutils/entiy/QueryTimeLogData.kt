package com.gx.app.gappxutils.entiy

import androidx.annotation.Keep

/**
 * Created by Android-小强 on 2021/11/15.
 * mailbox:980766134@qq.com
 * description:
 */
class QueryTimeLogData(
    var logId: String,
) {
    /**
     * 包名
     */
    @Keep
    var packageNames: String? = null

    /**
     * 用户id
     */
    @Keep
    var userId: String? = null

    /**
     * 开始时间
     */
    @Keep
    var startTime: Long? = null

    /**
     * 截至时间
     */
    @Keep
    var endTime: Long? = null

    /**
     * 查询出的结果
     */
    @Keep
    var queryResult: String? = null

    /**
     * 缓存中的时间
     */
    @Keep
    var cacheTime: Long? = null


    /**
     * 上报结果
     */
    @Keep
    var reportResults: String? = null

    /**
     * 此次查询完成结果
     */
    @Keep
    var resultsMsg: String? = null


}