package com.gx.app.gappxutils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gx.app.gappxutils.entiy.UserInfoData
import com.gx.app.gappxutils.utils.DataUtils

class UserActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, UserActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    val recyclerView: RecyclerView by lazy {
        findViewById(R.id.user_listview)
    }
    val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.user_shuaxin)
    }

    var listAdapter: UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        listAdapter = UserAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter

        swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launchWhenResumed {
            swipeRefreshLayout.isRefreshing = true
            try {
                val queryUserInfo = DataUtils.queryUserInfo()
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

class UserAdapter(val context: Context) : RecyclerView.Adapter<UserHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    val list = arrayListOf<UserInfoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.setData(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class UserHolder(val views: View) : RecyclerView.ViewHolder(views) {
    val tvContent: TextView = views.findViewById(R.id.item_user_tvmsg)
    val ivIcon: ImageView = views.findViewById(R.id.item_user_ivimag)
    fun setData(userInfoData: UserInfoData, position: Int) {

        Glide.with(ivIcon).load(userInfoData.avatar ?: "")
            .circleCrop()
            .into(ivIcon)

        tvContent.text = "$position : 用户id：${userInfoData.id}"
        tvContent.append("\n")
        tvContent.append("uid：${userInfoData.uid}")
        tvContent.append("\n")
        tvContent.append("登录状态：${userInfoData.loginState}")
        tvContent.append("\n")
        tvContent.append("用户名称：${userInfoData.name}")
        tvContent.append("\n")
        tvContent.append("用户邮箱：${userInfoData.email}")
        tvContent.append("\n")
        tvContent.append("师傅ID：${userInfoData.puid}")
        tvContent.append("\n")
        tvContent.append("用户性别：${userInfoData.gender}")
        tvContent.append("\n")
        tvContent.append("用户语言：${userInfoData.locale}")
        tvContent.append("\n")
        tvContent.append("总积分：${userInfoData.total}")
        tvContent.append("\n")
        tvContent.append("新用户奖励：${userInfoData.newUserReward}")
        tvContent.append("\n")
        tvContent.append("汇率：${userInfoData.newUserRate}")
        tvContent.append("\n")
        tvContent.append("最小提现金额：${userInfoData.minExchange}")
        tvContent.append("\n")
        tvContent.append("币种符号：${userInfoData.symbol}")
        tvContent.append("\n")
        tvContent.append("新人奖励弹框：${userInfoData.newUserRewardDialog}")
        tvContent.append("\n")
        tvContent.append("翻倍弹框：${userInfoData.oneDoubleDialog}")
        tvContent.append("\n")
        tvContent.append("提现时间：${userInfoData.lastExchangeTime}")
        tvContent.append("\n")
        tvContent.append("提现弹框：${userInfoData.showWithdrawalDialog}")
        tvContent.append("\n")
        tvContent.append("二次翻倍弹框：${userInfoData.doubleHint2}")
    }


}