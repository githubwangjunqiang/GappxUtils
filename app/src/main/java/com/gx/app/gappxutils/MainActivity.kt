package com.gx.app.gappxutils

import android.graphics.Color
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.gx.app.gappxutils.entiy.InItHttpData
import com.gx.app.gappxutils.utils.logd
import http.utils.LogInterceptor
import http.OkHttpUtils
import http.loadRequestBody
import http.postCall
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var switch: Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switch = findViewById(R.id.switch1)
        switch.isChecked = !App.APP_DEBUG
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            "switch-isChecked:$isChecked".logd()
            App.APP_DEBUG = !isChecked
            switch.setTextColor(if (isChecked) Color.WHITE else Color.BLACK)
        }
        OkHttpUtils.addTo = {
            it.addNetworkInterceptor(ParametricInterceptor())
        }
        OkHttpUtils.mInterceptors.add(LogInterceptor())
    }

    fun queryTimingLog(view: android.view.View) {
        LogQueryTimeActivity.startActivity(this)
    }

    fun queryUser(view: android.view.View) {
        UserActivity.startActivity(this)
    }

    fun queryTimeTask(view: android.view.View) {
        TaskActivity.startActivity(this)
    }

    fun queryfile(view: android.view.View) {
        FileActivity.startActivity(this)

    }

    fun testHttp(view: android.view.View) {

        lifecycleScope.launchWhenResumed {
            var json =
                "{\"plat\":\"1\",\"reqid\":\"7860cc2e-3116-4be6-86f0-6a0c80a6c322\",\"ts\":1637651244507,\"fit\":1637035197,\"zo\":480,\"session\":\"00000000-20bc-8ea1-0000-017d4b9ee5e1\",\"tz\":\"Asia\\/Shanghai\",\"uid\":\"Hzod9kv3mLJ6bD1WItncB\",\"flt\":1637554144952,\"dtype\":2,\"did\":\"3abc81fb-fe3e-46b4-a7fd-834032c5d2e0\",\"lang\":\"zh\",\"langname\":\"中文\",\"jb\":0,\"bundle\":\"com.gx.app.gappx.insidetest\",\"make\":\"Google\",\"brand\":\"google\",\"model\":\"Pixel 4a\",\"osv\":\"12\",\"build\":\"SP1A.211105.002\",\"appv\":\"1.5.0.4\",\"width\":1080,\"height\":2160,\"contype\":2,\"carrier\":\"\",\"mccmnc\":\"\",\"lcountry\":\"CN\",\"fm\":83121,\"btime\":1636983621355,\"ram\":5865259008,\"vpn\":0,\"hook\":0,\"debug\":1,\"organic\":0,\"afid\":\"1637554146217-6887490914047386900\",\"afms\":\"organic\",\"fbid\":\"\",\"cnl\":\"gappx_ssl\",\"wfr\":\"d0:76:e7:ff:f6:fc\\\"1tu1shu-2_5G\\\"\",\"fly\":0,\"ot\":0,\"as\":{\"os\":2,\"gp\":13,\"other\":340}}"

            "https://s.fuxiaos.com/init/v1".postCall(
                JSONObject(json).loadRequestBody(), InItHttpData::class.java
            ) {
                "error=${it.message}".logd()
            }?.let {
                "成功:${Gson().toJson(it)}".logd()
            }
        }

    }


}