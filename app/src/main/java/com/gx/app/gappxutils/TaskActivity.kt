package com.gx.app.gappxutils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gx.app.gappxutils.entiy.TaskDays
import com.gx.app.gappxutils.entiy.TimeTaskData
import com.gx.app.gappxutils.entiy.UserInfoData
import com.gx.app.gappxutils.utils.DataUtils
import com.gx.app.gappxutils.utils.format
import com.gx.app.gappxutils.utils.logd

class TaskActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TaskActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    val recyclerView: ExpandableListView by lazy {
        findViewById(R.id.user_listview)
    }
    val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.user_shuaxin)
    }

    var listAdapter: TaskAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        listAdapter = TaskAdapter(this)
        recyclerView.setAdapter(listAdapter)
        swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launchWhenResumed {
            swipeRefreshLayout.isRefreshing = true
            try {
                val queryUserInfo = DataUtils.queryTask()
                listAdapter?.list?.clear()
                listAdapter?.list?.addAll(queryUserInfo)
                listAdapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }
}

class TaskAdapter(val context: Context) : BaseExpandableListAdapter() {

    private val layoutInflater = LayoutInflater.from(context)
    val list = arrayListOf<TimeTaskData>()

    override fun getGroupCount(): Int {
        return list.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val size = list[groupPosition]?.taskDays?.size ?: 0
        "size:$size".logd()
        return size
    }

    override fun getGroup(groupPosition: Int): TimeTaskData {
        return list[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): TaskDays? {
        return list[groupPosition]?.taskDays?.get(childPosition) ?: null
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var holder: TaskHolder? = null
        var convertView: View? = convertView
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_task, null)
            holder = TaskHolder()
            holder?.ivIcon = convertView?.findViewById(R.id.task_item_iv)
            holder?.tvContent = convertView?.findViewById(R.id.task_item_tv)
            convertView?.tag = holder
        }
        if (holder == null) {
            holder = convertView?.tag as TaskHolder
        }
        holder?.setData(getGroup(groupPosition), groupPosition + 1)
        return convertView!!
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var holder: TaskChildHolder? = null
        var convertView: View? = convertView
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_task_child, null)
            holder = TaskChildHolder()
            holder?.tvContent = convertView?.findViewById(R.id.task_item_tvs)
            convertView?.tag = holder
        }
        if (holder == null) {
            holder = convertView?.tag as TaskChildHolder
        }
        holder?.setData(getChild(groupPosition, childPosition), childPosition + 1)
        return convertView!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}

class TaskHolder {
    var tvContent: TextView? = null
    var ivIcon: ImageView? = null
    fun setData(data: TimeTaskData, position: Int) {

        Glide.with(ivIcon!!).load(data.avatar ?: "")
            .circleCrop()
            .into(ivIcon!!)
        tvContent?.text = "$position : 用户id：${data.userId}"
        tvContent?.append("\n")
        tvContent?.append("进行中：${data.playing}")
        tvContent?.append("\n")
        tvContent?.append("offerId：${data.offerId}")
        tvContent?.append("\n")
        tvContent?.append("tid：${data.tid}")
        tvContent?.append("\n")
        tvContent?.append("包名：${data.packageNames}")
        tvContent?.append("\n")
        tvContent?.append("上次点击时间：${data.clickTime.format()}")
        tvContent?.append("\n")
        tvContent?.append("试玩时长（天）：${data.dayDuration}")
        tvContent?.append("\n")
        tvContent?.append("试玩时长（全部）：${data.allTime}")
        tvContent?.append("\n")
        tvContent?.append("任务是否已经完成：${data.complete}")
        tvContent?.append("\n")
        tvContent?.append("是否超时：${data.timeOut}")
        tvContent?.append("\n")
        tvContent?.append("上报打开app：${data.reportOpenOfferApp}")
    }


}

class TaskChildHolder {
    var tvContent: TextView? = null
    fun setData(data: TaskDays?, position: Int) {

        if (data == null) {
            return
        }
        tvContent?.text = "$position : 阶段id：${data?.step}"
        tvContent?.append("\n")
        tvContent?.append("阶段状态（是否完成）：${data.status == 1}")
        tvContent?.append("\n")
        tvContent?.append("第几天：${data.day}")
        tvContent?.append("\n")
        tvContent?.append("阶段时长（分钟）：${data.dur}")
        tvContent?.append("\n")
        tvContent?.append("开始时间：${data.startTime.format()}")
        tvContent?.append("\n")
        tvContent?.append("结束时间：${data.endTime.format()}")

        tvContent?.append("\n")
        tvContent?.append("是否已经计算过：${data.completeTime}")
        tvContent?.append("\n")
        tvContent?.append("是否已经上报：${data.completeHttp}")
        tvContent?.append("\n")
        tvContent?.append("当前阶段百分比：${data.progress}")

    }


}