package com.gx.app.gappxutils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gx.app.gappxutils.utils.GzipUtilsCache
import com.gx.app.gappxutils.utils.logd
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.lang.StringBuilder

class FileActivity : AppCompatActivity() {
    lateinit var listView: RecyclerView
    lateinit var mAistAdataer: ListAdataer
    lateinit var smartRefreshLayout: SmartRefreshLayout

    private var listData = arrayListOf<String>()

    companion object {
        var isTest = true
        var packageNames = "com.gx.app.gappx.insidetest"
        fun startActivity(context: Context) {

            context.startActivity(Intent(context, FileActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        packageNames = if (App.APP_DEBUG) {
            "com.gx.app.gappx.insidetest"
        } else {
            "com.gx.app.gappx"
        }





        smartRefreshLayout = findViewById(R.id.file_shuaxin)
        listView = findViewById(R.id.tvpushcontent)
        mAistAdataer = ListAdataer(this)
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = mAistAdataer


        try {
            var requestFileIntent = Intent(Intent.ACTION_PICK).apply {
                type = "image/jpg"
                `package` = packageNames
            }
            startActivityForResult(requestFileIntent, 1001)
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
            Toast.makeText(App.mContext, "打开失败", Toast.LENGTH_SHORT).show()
            return
        }


        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                setData()

                smartRefreshLayout.finishRefresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                val addData = addData()
                if (!addData) {
                    Toast.makeText(this@FileActivity, "没有更多了", Toast.LENGTH_SHORT).show()
                }
                smartRefreshLayout.finishLoadMore()

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            "onActivityResult".logd()
            lifecycleScope.launchWhenResumed {
                var list: List<String> = withContext(Dispatchers.IO) {
                    var lists = mutableListOf<String>()
                    try {
                        val zip = data?.getBooleanExtra("encrypt", false) ?: false
                        data?.data?.let { returnUri ->
                            val inputPFD = try {
                                /*
                                 * Get the content resolver instance for this context, and use it
                                 * to get a ParcelFileDescriptor for the file.
                                 */
                                contentResolver.openFileDescriptor(returnUri, "r")
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Log.e("MainActivity", "File not found.")
                                null
                            }

                            // Get a regular file descriptor for the file
                            val fd = inputPFD?.fileDescriptor
                            val fileInputStream = FileInputStream(fd)
                            val readBytes = fileInputStream.readBytes()
                            val string = if (zip) {
                                val unGZip = GzipUtilsCache.unGZip(readBytes)
                                String(unGZip)
                            } else {
                                String(readBytes)
                            }
                            val appVersionName = getAppVersionName(App.mContext, packageNames)
                            var moreThanThe: Boolean = appVersionName?.let {
                                val split = it.split(".")
                                val stringBuilder = StringBuilder()
                                for (index in split.indices) {
                                    if (index == 0) {
                                        stringBuilder.append(split[index]).append(".")
                                    } else {
                                        stringBuilder.append(split[index])
                                    }
                                }
                                val code: Double = 1.503
                                val toDoubleOrNull =
                                    stringBuilder.toString().toDoubleOrNull() ?: code
                                toDoubleOrNull > code
                            } ?: false


                            "moreThanThe:$moreThanThe".logd()
                            val dataProcessing = dataProcessing(string, moreThanThe || zip)
                            "readText=:${dataProcessing.size}".logd()
                            lists.addAll(dataProcessing)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    lists
                }
                listData.clear()
                listData.addAll(list)
                setData()
            }

        }
    }

    private var index = 0

    private fun setData() {
        index = 0
        mAistAdataer.list.clear()
        if (listData.isNotEmpty()) {
            mAistAdataer.list.add(listData[index])
        }
        mAistAdataer.notifyDataSetChanged()
        index++
        "setData".logd()
    }

    private fun addData(): Boolean {
        if (listData.size <= index) {
            return false
        }
        mAistAdataer.list.add(listData[index])
        index++
        mAistAdataer.notifyDataSetChanged()
        return true
    }

    /**
     * 处理数据
     */
    private fun dataProcessing(string: String, enableDecompression: Boolean): List<String> {
        val splits = string.split("GAPPX")
        val arrayListOf = arrayListOf<String>()
        val filter = splits.filter {
            !TextUtils.isEmpty(it)
        }
        arrayListOf.addAll(filter)



        if (enableDecompression) {
            for (index in arrayListOf.indices) {
                var data = arrayListOf[index]
                if (data.length > 2000) {
                    data = data.substring(0, 2000)
                    arrayListOf[index] = data
                }
            }
        }


        return arrayListOf

    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    fun getAppVersionName(context: Context, packageName: String?): String? {
        return if (TextUtils.isEmpty(packageName)) {
            null
        } else try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName!!, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}

class ListAdataer(val context: Context) : RecyclerView.Adapter<ListViewHolders>() {

    var list = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolders {
        return ListViewHolders(
            LayoutInflater.from(context).inflate(R.layout.item_list_tv_msg, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolders, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int = list.size

}

class ListViewHolders(val views: View) : RecyclerView.ViewHolder(views) {
    val tvContent: TextView = views.findViewById(R.id.item_list_tv)
    fun setData(s: String) {
        tvContent.text = s
    }


}