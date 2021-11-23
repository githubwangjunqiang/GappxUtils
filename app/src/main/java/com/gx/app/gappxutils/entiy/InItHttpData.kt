package com.gx.app.gappxutils.entiy

import androidx.annotation.Keep

/**
 * Created by Android-小强 on 2021/5/25.
 * mailbox:980766134@qq.com
 * description: 初始化接口 需要的实体类
 */
@Keep
class InItHttpData : ReturnData<Any>() {
    /**
     * 是否需要login,0:否，1:是
     */
    @Keep
    var login: Int? = null

    /**
     * H5页面域名及跟路径 BaseUri
     */
    @Keep
    var base: String? = null

    /**
     * 是否需要更新 0:否，1:是
     */
    @Keep
    var upgrade: Int? = null

    /**
     * 首页banner 集合
     */
    @Keep
    var bs: ArrayList<HomeBanner>? = null

    /**
     * 是否需要更新
     */
    fun loadNeedUpData(): Boolean {
        return upgrade == 1
    }

    /**
     * force upgrade是否强制升级,0:否，1:是
     */
    @Keep
    var fu: Int? = null

    /**
     * 是否  强制更新
     */
    fun loadNeedMandatoryUpdate(): Boolean {
        return fu == 1
    }

    /**
     * 常规 app 集合
     */
    @Keep
    var gas: List<String>? = null


    /**
     * 最小提现金币数
     */
    @Keep
    var minExchangeGold: Long? = null
}

const val ADLOE = 1
const val ADGEM = 2

@Keep
class HomeBanner {
    @Keep
    var img: String? = null

    /**
     * 1-> adjoe  2->AdGem
     */
    @Keep
    var type: Int? = 0
}