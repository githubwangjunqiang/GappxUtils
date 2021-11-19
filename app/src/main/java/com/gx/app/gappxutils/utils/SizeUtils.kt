package com.gx.app.gappxutils.utils

import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast
import com.gx.app.gappxutils.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Android-小强 on 2021/11/15.
 * mailbox:980766134@qq.com
 * description: 尺寸工具类
 */


/**
 * px 转换 dp
 */
fun Float.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.mContext.resources.displayMetrics
    )
}

/**
 * px 转换 dp
 */
fun Float.dpInt(): Int {
    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.mContext.resources.displayMetrics
    ) + 0.5F).toInt()
}
/**
 * px 转换 dp
 */
fun Long?.format(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(this ?: 0)
}

/**
 * 显示 土司提示
 *
 * @param string
 */
fun String?.show() {
    this?.let {
        GlobalScope.launch(Dispatchers.Main) {
            var toast = Toast.makeText(App.mContext, it, Toast.LENGTH_SHORT)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                toast?.setGravity(Gravity.CENTER, 0, 0)
            }
            toast?.show()
        }

    }
}

/**
 * 显示 土司提示
 *
 * @param string
 */
fun String?.logd() {
    this?.let {
        Log.d("12345", "logd: $it")

    }
}