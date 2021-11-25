package http.utils

import com.google.gson.Gson
import http.OkHttpUtils
import http.exception.MyEmptyException
import http.exception.MyParsingException
import http.exception.MyServiceCodeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * Created by Android-小强 on 2021/11/24.
 * mailbox:980766134@qq.com
 * description: 请求工具
 */
object CallUtils {
    /**
     * 拉去请求
     */
    suspend fun <T> loadResult(
        url: String,
        body: RequestBody,
        clazz: Class<T>?
    ): T? {
        var result = withContext(Dispatchers.IO) {
            var string: String? = null
            val nanoTime = System.currentTimeMillis()
            try {
                string = OkHttpUtils.postCall(url, body)
                string = executionInterceptor(url, nanoTime, string)
            } catch (e: Exception) {
                executionInterceptor(url, nanoTime, string, e)
                throw e
            }

            when {
                clazz == null -> {
                    null
                }
                string.isNullOrEmpty() -> {
                    throw MyEmptyException()
                }
                else -> {
                    string.testResults()
                    string.analysisResult(clazz)
                }
            }
        }
        return result
    }

    /**
     * 执行返回数据拦截器
     */
    fun executionInterceptor(
        url: String,
        startTime: Long,
        value: String?,
        e: Exception? = null
    ): String? {
        val iterator = OkHttpUtils.mInterceptors.iterator()
        var string = value
        while (iterator.hasNext()) {
            val next = iterator.next()
            try {
                string = next.intercept(url, startTime, string, e)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return string
    }

    /**
     * 解析结果
     */
    fun <T> String.analysisResult(clazz: Class<T>? = null): T? {

        return clazz?.let {
            try {
                when (it) {
                    String::class.java -> {
                        this as T
                    }
                    Boolean::class.java -> {
                        (this.toBooleanStrictOrNull() ?: false) as T
                    }
                    else -> {
                        Gson().fromJson<T>(this, clazz)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw MyParsingException(e)
            }
        }

    }

    /**
     * 检查结果
     */
    fun String.testResults(): Boolean {

        val jsonObject = JSONObject(this)

        val code = jsonObject.optInt(CallPool.HTTP_RETURN_CODE, CallPool.HTTP_RETURN_CODE_SUCCESS)
        var msg = jsonObject.optString(CallPool.HTTP_RETURN_MSG, "")
        if (code != CallPool.HTTP_RETURN_CODE_SUCCESS) {
            throw MyServiceCodeException(code, msg)
        }
        return true
    }

    /**
     * 压缩
     */
    fun zip(byteArray: ByteArray): ByteArray {
        return GzipUtils.inGZip(byteArray)
    }

    /**
     * 加密
     */
    fun aesData(byteArray: ByteArray): ByteArray {
        val encrypt = try {
            ABDECF.encrypt(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
            "".toByteArray()
        }
        return encrypt
    }
}