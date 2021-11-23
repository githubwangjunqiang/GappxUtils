package com.gx.app.gappxutils.utils

import android.database.Cursor
import android.net.Uri
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gx.app.gappxutils.App
import com.gx.app.gappxutils.entiy.QueryTimeLogData
import com.gx.app.gappxutils.entiy.TaskDays
import com.gx.app.gappxutils.entiy.TimeTaskData
import com.gx.app.gappxutils.entiy.UserInfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Android-小强 on 2021/11/15.
 * mailbox:980766134@qq.com
 * description:
 */
object DataUtils {
    /**
     *  测试的
     */
    val BASE_URI_TEST = Uri.parse("content://com.gx.app.gappx.insidetest.timeing_provider")

    /**
     * 正式的
     */
    val BASE_URI_APP = Uri.parse("content://com.gx.app.gappx.timeing_provider")

    /**
     * 计时日志
     */
    var time_log_uri: Uri = Uri.parse("")
        get() {
            field = if (App.APP_DEBUG) {
                Uri.withAppendedPath(BASE_URI_TEST, "log")
            } else {
                Uri.withAppendedPath(BASE_URI_APP, "log")
            }
            return field
        }

    /**
     * 用户信息
     */
    var userinfo_uri: Uri = Uri.parse("")
        get() {
            field = if (App.APP_DEBUG) {
                Uri.withAppendedPath(BASE_URI_TEST, "user")
            } else {
                Uri.withAppendedPath(BASE_URI_APP, "user")
            }
            return field
        }


    /**
     * 本地任务
     */
    var task_uri: Uri = Uri.parse("")
        get() {
            field = if (App.APP_DEBUG) {
                Uri.withAppendedPath(BASE_URI_TEST, "task")
            } else {
                Uri.withAppendedPath(BASE_URI_APP, "task")
            }
            return field
        }

    /**
     * 获取数据
     */
    suspend fun queryReportLog(): List<QueryTimeLogData>? {
        return withContext(Dispatchers.IO) {
            val list = arrayListOf<QueryTimeLogData>()
            val query = App.mContext.contentResolver.query(
                time_log_uri, null, null,
                null, null
            )
            "query:$query".logd()
            if (query == null) {
                list
            }

            try {
                while (query?.moveToNext() == true) {
                    val logId = query.getColumnIndex("logId").let {
                        if (it != -1) {
                            query.getStringOrNull(it) ?: ""
                        } else {
                            ""
                        }
                    }
                    val queryTimeLogData = QueryTimeLogData(logId)
                    try {
                        queryTimeLogData.packageNames = query.getColumnIndex("packageNames").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        queryTimeLogData.userId = query.getColumnIndex("userId").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        queryTimeLogData.startTime = query.getColumnIndex("startTime").let {
                            if (it != -1) {
                                query.getLongOrNull(it)
                            } else {
                                null
                            }
                        }
                        queryTimeLogData.endTime = query.getColumnIndex("endTime").let {
                            if (it != -1) {
                                query.getLongOrNull(it)
                            } else {
                                null
                            }
                        }
                        queryTimeLogData.queryResult = query.getColumnIndex("queryResult").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        queryTimeLogData.cacheTime = query.getColumnIndex("cacheTime").let {
                            if (it != -1) {
                                query.getLongOrNull(it)
                            } else {
                                null
                            }
                        }
                        queryTimeLogData.reportResults = query.getColumnIndex("reportResults").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        queryTimeLogData.resultsMsg = query.getColumnIndex("resultsMsg").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    list.add(queryTimeLogData)
                }
            } catch (e: Exception) {
            } finally {
                closeQuery(query)
            }
            list
        }
    }

    private fun closeQuery(query: Cursor?) {

        try {
            query?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取数据
     */
    suspend fun queryUserInfo(): List<UserInfoData> {
        return withContext(Dispatchers.IO) {
            val list = arrayListOf<UserInfoData>()
            val query = App.mContext.contentResolver.query(
                userinfo_uri, null, null,
                null, null
            )
            "query-queryUserInfo:$query".logd()
            if (query == null) {
                list
            }

            try {
                while (query?.moveToNext() == true) {
                    query?.columnNames.forEach {
                        "$it".logd()
                    }
                    val logId = query.getColumnIndex("userId").let {
                        if (it != -1) {
                            query.getStringOrNull(it) ?: ""
                        } else {
                            ""
                        }
                    }
                    val loginStats = query.getColumnIndex("loginState").let {
                        if (it != -1) {
                            query.getIntOrNull(it) ?: 0
                        } else {
                            0
                        }
                    }
                    val data = UserInfoData(logId, loginStats == 1)
                    try {
                        data.name = query.getColumnIndex("name").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.id = query.getColumnIndex("showId").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.email = query.getColumnIndex("email").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.puid = query.getColumnIndex("puid").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.avatar = query.getColumnIndex("avatar").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.gender = query.getColumnIndex("gender").let {
                            if (it != -1) {
                                query.getIntOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.locale = query.getColumnIndex("locale").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.balance = query.getColumnIndex("balance").let {
                            if (it != -1) {
                                query.getLongOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.expend = query.getColumnIndex("expend").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.total = query.getColumnIndex("total").let {
                            if (it != -1) {
                                query.getLongOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.newUserReward = query.getColumnIndex("newUserReward").let {
                            if (it != -1) {
                                query.getIntOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.newUserRate = query.getColumnIndex("newUserRate").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.minExchange = query.getColumnIndex("minExchange").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.symbol = query.getColumnIndex("symbol").let {
                            if (it != -1) {
                                query.getStringOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.newUserRewardDialog = query.getColumnIndex("newUserRewardDialog").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                null
                            }
                        }
                        data.oneDoubleDialog = query.getColumnIndex("oneDoubleDialog").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                null
                            }
                        }
                        data.lastExchangeTime = query.getColumnIndex("lastExchangeTime").let {
                            if (it != -1) {
                                query.getLongOrNull(it)
                            } else {
                                null
                            }
                        }
                        data.showWithdrawalDialog =
                            query.getColumnIndex("showWithdrawalDialog").let {
                                if (it != -1) {
                                    query.getIntOrNull(it) == 1
                                } else {
                                    null
                                }
                            }
                        data.doubleHint2 = query.getColumnIndex("doubleHint2").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                null
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    list.add(data)
                }
            } finally {
                closeQuery(query)
            }
            "list-size=${list.size}".logd()
            list.apply {
                this.sortBy {
                    !it.loginState
                }
            }
        }
    }

    /**
     * 获取数据
     */
    suspend fun queryTask(): List<TimeTaskData> {
        return withContext(Dispatchers.IO) {
            val list = arrayListOf<TimeTaskData>()
            val query = App.mContext.contentResolver.query(
                task_uri, null, null,
                null, null
            )
            "query-queryUserInfo:$query".logd()
            if (query == null) {
                list
            }
            val gson = Gson()
            var contentJson = ""
            try {
                while (query?.moveToNext() == true) {
                    val uid = query.getColumnIndex("userId").let {
                        if (it != -1) {
                            query.getStringOrNull(it) ?: ""
                        } else {
                            ""
                        }
                    }
                    val tid = query.getColumnIndex("tid").let {
                        if (it != -1) {
                            query.getStringOrNull(it) ?: ""
                        } else {
                            ""
                        }
                    }
                    val oid = query.getColumnIndex("offerId").let {
                        if (it != -1) {
                            query.getStringOrNull(it) ?: ""
                        } else {
                            ""
                        }
                    }
                    val data = TimeTaskData(uid, oid, tid)
                    try {
                        data.packageNames = query.getColumnIndex("packageNames").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.avatar = query.getColumnIndex("avatar").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.clickTime = query.getColumnIndex("clickTime").let {
                            if (it != -1) {
                                query.getLongOrNull(it) ?: 0
                            } else {
                                0
                            }
                        }
                        data.dayDuration = query.getColumnIndex("dayDuration").let {
                            if (it != -1) {
                                query.getLongOrNull(it) ?: 0
                            } else {
                                0
                            }
                        }
                        data.allTime = query.getColumnIndex("allTime").let {
                            if (it != -1) {
                                query.getLongOrNull(it) ?: 0
                            } else {
                                0
                            }
                        }
                        data.playing = query.getColumnIndex("playing").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                false
                            }
                        }
                        data.complete = query.getColumnIndex("complete").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                false
                            }
                        }
                        data.timeOut = query.getColumnIndex("timeOut").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                false
                            }
                        }
                        data.reportOpenOfferApp = query.getColumnIndex("reportOpenOfferApp").let {
                            if (it != -1) {
                                query.getIntOrNull(it) == 1
                            } else {
                                false
                            }
                        }
                        contentJson = query.getColumnIndex("taskDays").let {
                            if (it != -1) {
                                query.getStringOrNull(it) ?: ""
                            } else {
                                ""
                            }
                        }
                        data.taskDays = try {
                            gson.fromJson<ArrayList<TaskDays>>(
                                contentJson,
                                object : TypeToken<ArrayList<TaskDays>>() {}.type
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    list.add(data)
                }
            } finally {
                closeQuery(query)
            }
            list.apply {
                this.sortBy {
                    !it.playing
                }
            }
        }
    }
}