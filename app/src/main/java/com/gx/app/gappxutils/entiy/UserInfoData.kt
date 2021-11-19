package com.gx.app.gappxutils.entiy

import androidx.annotation.Keep


/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/17 14:40
 */
@Keep
data class UserInfoData(
    @Keep
    var uid: String,
    /**
     * 登录状态
     */
    @Keep
    var loginState: Boolean = false,
) {

    /**
     * 用户名称
     */
    @Keep
    var name: String? = null

    /**
     * 用户显示id
     */
    @Keep
    var id: String? = null

    /**
     * 用户 邮箱
     */
    @Keep
    var email: String? = null

    /**
     * 上级用户id
     */
    @Keep
    var puid: String? = null

    /**
     * 头像地址
     */
    @Keep
    var avatar: String? = null

    /**
     * 性别  0:未知, 男:1, 女:2
     */
    @Keep
    var gender: Int? = null

    /**
     *   语种 示例  en
     */
    @Keep
    var locale: String? = null

    /**
     *   剩余积分
     */
    @Keep
    var balance: Long? = null

    /**
     *   已经兑换积分
     */
    @Keep
    var expend: String? = null

    /**
     *   总积分
     */
    @Keep
    var total: Long? = null

    /**
     *   新用户奖励
     */
    @Keep
    var newUserReward: Int? = null

    /**
     *   汇率
     */
    @Keep
    var newUserRate: String? = null

    /**
     *   最小提现金额
     */
    @Keep
    var minExchange: String? = null

    /**
     *   币种符号
     */
    @Keep
    var symbol: String? = null

    /**
     *   是否弹出新人奖励弹框
     */
    @Keep
    var newUserRewardDialog: Boolean? = null

    /**
     *   是否弹出翻倍弹框
     */
    @Keep
    var oneDoubleDialog: Boolean? = null

    /**
     *   提现时间  如果大于0 那就是提过现，其他情况是没有提现过
     */
    @Keep
    var lastExchangeTime: Long? = null

    /**
     *   是否展示过 提现目标达到的弹框
     */
    @Keep
    var showWithdrawalDialog: Boolean? = null

    /**
     *   是否展示过 第二次翻倍
     */
    @Keep
    var doubleHint2: Boolean? = null


}

