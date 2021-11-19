package com.gx.app.gappxutils

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gx.app.gappxutils.entiy.QueryTimeLogData
import com.gx.app.gappxutils.utils.DataUtils
import com.gx.app.gappxutils.utils.show
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Android-小强 on 2021/11/15.
 * mailbox:980766134@qq.com
 * description:
 */
class LogQueryTimeActivity : AppCompatActivity() {

    companion object {
        const val TAG_TYPE = "TAG_TYPE"
        fun startActivity(context: Context, type: Int = -1) {
            context.startActivity(Intent(context, LogQueryTimeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(TAG_TYPE, type)
            })
        }
    }

    private var listAdapter: ListAdapterReport? = null
    val listView: RecyclerView by lazy {
        findViewById(R.id.recyleview_repotr_msg)
    }
    val tvTop: TextView by lazy {
        findViewById(R.id.app_log_tv_top)
    }
    val ivRe: ImageView by lazy {
        findViewById(R.id.app_log_tv_rehhse)
    }
    val tvNum: TextView by lazy {
        findViewById(R.id.app_log_tv_sum)
    }

    //    val smartLayout: SwipeRefreshLayout by lazy {
//        findViewById(R.id.smart_repotr_msg)
//    }
    val barListView: BarListView by lazy {
        findViewById(R.id.app_log_bar_listview)
    }


    var animation: ObjectAnimator? = null

    override fun onDestroy() {
        animation?.cancel()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_query_time)
        barListView.drawableRes = R.drawable.app_log_qurey_bar_listview
        barListView.listener = {
            val d = it * 0.01F
            val size = listAdapter?.list?.size ?: 0
            if (size > 0) {
                val fl: Int = (size * d).toInt()
                val maxOf = maxOf(0, minOf(size - 1, fl))
                barListView.text = "${maxOf + 1}"
                listView.scrollToPosition(maxOf)
            }
        }

        tvTop.setOnClickListener {
            if (listAdapter?.itemCount ?: 0 > 0) {
                listView.scrollToPosition(0)
                barListView.topSize = 0
                barListView.invalidate()
            }

        }
        ivRe.setOnClickListener {
            loadData()
        }


        listAdapter = ListAdapterReport(this)
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = listAdapter



        listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

        })

        ivRe.post {
            loadData()
        }
    }

    private fun loadData() {
        if (animation == null) {
            animation = ObjectAnimator.ofFloat(ivRe, View.ROTATION, 0F, 360F).apply {
                duration = 600
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
            }
        }
        animation?.start()
        lifecycleScope.launchWhenCreated {
            val type = intent.getIntExtra(TAG_TYPE, -1)
            if (type == -1) {
                val queryReportLog = DataUtils.queryReportLog()
                if (queryReportLog.isNullOrEmpty()) {
                    "数据库没有数据".show()
                    animation?.cancel()
                    listAdapter?.list?.clear()
                    listAdapter?.notifyDataSetChanged()
                } else {
                    listAdapter?.list?.clear()
                    listAdapter?.list?.addAll(queryReportLog)
                    listAdapter?.notifyDataSetChanged()
                }
                tvNum.text = "  ${queryReportLog?.size ?: 0} 条"
            } else {
                listAdapter?.list?.clear()
                listAdapter?.type = type
                listAdapter?.notifyDataSetChanged()
            }
            animation?.cancel()

            "查询完毕".show()
        }

    }
}

private class ListAdapterReport : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var type: Int = -1
    val list: ArrayList<QueryTimeLogData> = arrayListOf()

    private lateinit var layoutInflater: LayoutInflater

    constructor(context: Context) : super() {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (type == 0)
            ListViewHolderHttp(
                layoutInflater.inflate(
                    R.layout.item_report_msg_list,
                    parent,
                    false
                )
            )
        else
            ListViewHolder(layoutInflater.inflate(R.layout.item_report_msg_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (type == -1) {
            (holder as ListViewHolder).setData(list[position], position)
        } else {
//            (holder as ListViewHolderHttp).tvError?.text = list[position].error
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(reportLog: ArrayList<QueryTimeLogData>) {

        val diffCallback = DiffCallback(list, reportLog)
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)
        list.clear()
        list.addAll(reportLog)


    }

}

private class DiffCallback(
    val listOld: ArrayList<QueryTimeLogData>,
    val listnew: ArrayList<QueryTimeLogData>,
) :
    DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return listOld.size
    }

    override fun getNewListSize(): Int {
        return listnew.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listOld[oldItemPosition].logId == listnew[newItemPosition].logId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listOld[oldItemPosition].logId == listnew[newItemPosition].logId
    }

}

private class ListViewHolder : RecyclerView.ViewHolder {

    var tvTitle: TextView? = null
    var tvUid: TextView? = null
    var tvType: TextView? = null
    var tvGid: TextView? = null
    var tvOid: TextView? = null
    var tvPageName: TextView? = null
    var tvLocaAllTime: TextView? = null
    var tvLocaMsg: TextView? = null
    var tvStartTime: TextView? = null
    var tvEndTime: TextView? = null
    var tvQueryTime: TextView? = null
    var tvCacheTime: TextView? = null
    var tvZipCacheTime: TextView? = null
    var tvQueryMsg: TextView? = null
    var tvReportHttpResults: TextView? = null
    var tvReportHttpMsg: TextView? = null
    var tvReportOver: TextView? = null
    var tvError: TextView? = null
    var tvPerission: TextView? = null

    constructor(itemView: View) : super(itemView) {
        tvTitle = itemView.findViewById(R.id.item_msg_logId)
        tvUid = itemView.findViewById(R.id.item_msg_userId)
        tvType = itemView.findViewById(R.id.item_msg_type)
        tvGid = itemView.findViewById(R.id.item_msg_gameId)
        tvOid = itemView.findViewById(R.id.item_msg_orderId)
        tvPageName = itemView.findViewById(R.id.item_msg_packageNames)
        tvLocaAllTime = itemView.findViewById(R.id.item_msg_localAllTime)
        tvLocaMsg = itemView.findViewById(R.id.item_msg_localMsg)
        tvStartTime = itemView.findViewById(R.id.item_msg_queryStartTime)
        tvEndTime = itemView.findViewById(R.id.item_msg_queryEndTime)
        tvQueryTime = itemView.findViewById(R.id.item_msg_querySystemTime)
        tvCacheTime = itemView.findViewById(R.id.item_msg_localCacheDataTime)
        tvZipCacheTime = itemView.findViewById(R.id.item_msg_zipLocalAndQueryTime)
        tvQueryMsg = itemView.findViewById(R.id.item_msg_queryMsg)
        tvReportHttpResults = itemView.findViewById(R.id.item_msg_reportHttpResults)
        tvReportHttpMsg = itemView.findViewById(R.id.item_msg_reportHttpMsg)
        tvReportOver = itemView.findViewById(R.id.item_msg_reportOver)
        tvError = itemView.findViewById(R.id.item_msg_error)
        tvPerission = itemView.findViewById(R.id.item_msg_competence)
    }

    fun setData(reportLog: QueryTimeLogData?, position: Int) {
        reportLog?.run {

            tvTitle?.text = "${position + 1}:生成时间：${
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.ENGLISH
                ).format(logId.toLong())
            }"
            tvUid?.text = "用户id：$userId"

            tvType?.text = "记录类型：$"
            tvType?.visibility = View.GONE

            tvGid?.text = "游戏ID：$"
            tvGid?.visibility = View.GONE

            tvOid?.text = "订单ID：$"
            tvOid?.visibility = View.GONE

            tvPageName?.text = "记录包名：$packageNames"

            tvLocaAllTime?.text = "本地任务总时长：$"
            tvLocaAllTime?.visibility = View.GONE

//            tvLocaMsg?.text = "查询本地任务结果：$queryResult"

            setTextColor(tvLocaMsg, "查询本地任务结果：$queryResult")

            startTime?.let {
                tvStartTime?.text =
                    "本次记录查询开始时间：${
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.ENGLISH
                        ).format(it)
                    }"
            }
            endTime?.let {
                tvEndTime?.text =
                    "本次记录查询结束时间：${
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.ENGLISH
                        ).format(endTime)
                    }"
            }
            tvQueryTime?.text = "本次记录查询结果时间：$queryResult"
            tvQueryTime?.visibility = View.GONE
            tvCacheTime?.text = "未上报缓存表时长：$cacheTime"
            tvCacheTime?.visibility = View.GONE

            tvZipCacheTime?.text = "组装缓存后时长：$queryResult"
            tvZipCacheTime?.visibility = View.GONE

            tvQueryMsg?.text = "本次查询系统时间结果：$queryResult"
            tvQueryMsg?.visibility = View.GONE


            tvReportHttpResults?.text = "接口上报结果：$reportResults"

            tvReportHttpMsg?.text = "联网上报结果：$reportResults"
            tvReportHttpMsg?.visibility = View.GONE

            tvReportOver?.text = "上报完毕：$reportResults"
            tvReportOver?.visibility = View.GONE
            tvError?.text = "出现错误：$resultsMsg"

            tvPerission?.text = "有没有权限：$"
            tvPerission?.visibility = View.GONE
        }

    }

    private fun setTextColor(tvLocaMsg: TextView?, s: String) {

        val builder = SpannableStringBuilder(s)
        val s1 = "queryTime:"

        val indexOf = builder.indexOf(s1, 0, true)

        if (indexOf == -1) {
            tvLocaMsg?.text = s
            return
        }

        //指定位置的字体 大小
        val absoluteSizeSpan = AbsoluteSizeSpan(16, true)
        builder.setSpan(
            absoluteSizeSpan,
            indexOf,
            s1.length + indexOf,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        //指定位置的字体颜色
        val spanColor = ForegroundColorSpan(Color.parseColor("#ff0000"))
        builder.setSpan(spanColor, indexOf, s1.length + indexOf, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        tvLocaMsg?.text = builder
    }


}

private class ListViewHolderHttp : RecyclerView.ViewHolder {

    var tvError: TextView? = null

    constructor(itemView: View) : super(itemView) {
        tvError = itemView.findViewById(R.id.item_msg_logId)
    }

//    fun setData(reportLog: ReportLog?) {
//        reportLog?.run {
//            tvError?.text = "$error"
//        }
//
//    }


}