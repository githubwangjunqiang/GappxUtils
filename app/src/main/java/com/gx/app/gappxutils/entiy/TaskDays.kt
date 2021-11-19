package com.gx.app.gappxutils.entiy

import androidx.annotation.Keep

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/21 10:39
 */
@Keep
class TimeTaskData(
    /**
     * 用户id
     */
    @Keep
    var userId: String,
    /**
     * offerId  每个任务唯一的
     */
    @Keep
    var offerId: String,
    /**
     * tid  每个任务唯一的
     */
    @Keep
    var tid: String,
) {

    /**
     * 游戏的包名
     */
    @Keep
    var packageNames: String = ""

    /**
     * 任务的头像
     */
    @Keep
    var avatar: String = ""

    /**
     * 上次点击开始游戏的 时间戳
     */
    @Keep
    var clickTime: Long = 0L

    /**
     * 当天试玩时间
     */
    @Keep
    var dayDuration: Long = 0L

    /**
     * 试玩总时长
     */
    @Keep
    var allTime: Long = 0L

    /**
     * 此游戏任务 是否正在进行中
     */
    @Keep
    var playing: Boolean = false

    /**
     * 任务阶段
     */
    @Keep
    var taskDays: ArrayList<TaskDays>? = null

    /**
     * 任务是否已经完成
     */
    @Keep
    var complete: Boolean? = false

    /**
     * 任务是否已经超时
     */
    @Keep
    var timeOut: Boolean? = false

    /**
     * 是否上包过 打开app
     */
    @Keep
    var reportOpenOfferApp: Boolean? = false

}


/**
 * 阶段  试玩天数 和每天的时间阶段
 */
@Keep
class TaskDays {
    /**
     * 阶段 id
     */
    @Keep
    var step: Int = 1

    /**
     * 阶段 id
     */
    @Keep
    var day: Int = 1

    /**
     * 时长 分钟
     */
    @Keep
    var dur: Long? = 0

    /**
     * 当前阶段 开始查询时间戳 一般第一天为点击开始时间 后续第二天等 是当天时间戳
     */
    @Keep
    var startTime: Long? = 0

    /**
     * 当前阶段 结束时间戳 就是当天夜里12点 停止查询
     */
    @Keep
    var endTime: Long? = 0

    /**
     * 阶段状态 0:未开始,1:已完成
     */
    @Keep
    var status: Int? = 0


    /**
     * 当前阶段 的时间已经达到了 计时已经计算过了
     */
    @Keep
    var completeTime: Boolean? = false

    /**
     * 当前阶段 是否已经完成上报
     */
    @Keep
    var completeHttp: Boolean? = false

    /**
     * 当前阶段 百分比
     */
    @Keep
    var progress: Long? = 0

}
