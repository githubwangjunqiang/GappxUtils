package http

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.google.gson.Gson
import com.gx.app.gappxutils.utils.logd
import http.exception.MyEmptyException
import http.exception.MyHttpCodeException
import http.exception.MyParsingException
import http.exception.MyServiceCodeException
import http.utils.ABDEC
import http.utils.DataStoreCookiePersistor
import http.utils.GzipUtils
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Android-小强 on 2021/11/22.
 * mailbox:980766134@qq.com
 * description: 网络管理器
 */

object OkHttpUtils : IOkHttpUtils {
    @Volatile
    var addTo: ((builder: OkHttpClient.Builder) -> Unit)? = null

    /**
     * 全局管理器
     */
    val okhttpManager: OkHttpClient by lazy {
        createHttpClient(addTo)
    }

    val mInterceptors: LinkedList<IMyInterceptor> by lazy {
        LinkedList()
    }

    override fun createHttpClient(addTo: ((builder: OkHttpClient.Builder) -> Unit)?): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cookieJar(PersistentCookieJar(SetCookieCache(), DataStoreCookiePersistor()))
        addTo?.invoke(builder)
        return builder.build()
    }

    override fun postCall(url: String, body: RequestBody): String? {
        var string: String? = null
        var client: OkHttpClient = okhttpManager
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val newCall = client.newCall(request)
        val execute = newCall.execute()
        if (execute.code == Pool.HTTP_OK) {
            string = execute.body?.string()
        } else {
            throw MyHttpCodeException(execute.code, execute.message)
        }
        return string
    }

    override fun getCall(url: String, map: Map<String, String>?): String? {
        var string: String? = null
        val toHttpUrlOrNull = url.toHttpUrlOrNull()!!
        val newBuilder = toHttpUrlOrNull.newBuilder()
        map?.forEach {
            newBuilder.addQueryParameter(it.key, it.value)
        }
        var client: OkHttpClient = okhttpManager
        val request = Request.Builder()
            .url(newBuilder.build())
            .build()
        val newCall = client.newCall(request)
        val execute = newCall.execute()
        if (execute.code == Pool.HTTP_OK) {
            string = execute.body?.string()
        } else {
            throw MyHttpCodeException(execute.code, execute.message)
        }
        return string
    }


}

/**
 * 请求工具类
 */
private object Utils {
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
        "iterator;${OkHttpUtils.mInterceptors.size}".logd()
        var string = value
        while (iterator.hasNext()) {
            val next = iterator.next()
            try {
                string = next.intercept(url, startTime, value, e)
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

        val code = jsonObject.optInt(Pool.HTTP_RETURN_CODE, Pool.HTTP_RETURN_CODE_SUCCESS)
        var msg = jsonObject.optString(Pool.HTTP_RETURN_MSG, "")
        if (code != Pool.HTTP_RETURN_CODE_SUCCESS) {
            throw MyServiceCodeException(code, msg)
        }
        return true
    }
}

/**
 * 常量池
 */
object Pool {
    /**
     * 请求头 流 value
     */
    const val HEADER_STREAM_VALUE = "application/octet-stream"

    /**
     * 请求通用状态码 http 200
     */
    const val HTTP_OK = 200

    /**
     * 返回码的 key
     */
    const val HTTP_RETURN_CODE = "code"

    /**
     * 返回信息的 key
     */
    const val HTTP_RETURN_MSG = "msg"

    /**
     * 返回码的 key 成功值
     */
    const val HTTP_RETURN_CODE_SUCCESS = 0

    /**
     * 返回失败
     */
    const val HTTP_RETURN_CODE_ERROR = -1
}


/**
 *请求接口
 */
suspend fun <T> String.postCall(
    body: RequestBody,
    clazz: Class<T>? = null,
    error: ((e: Exception) -> Unit)? = null,
): T? {

    val url = this
    var result: T? = null
    try {
        result = Utils.loadResult(url, body, clazz)
    } catch (e: Exception) {
        e.printStackTrace()
        when (e) {
            is CancellationException -> {
            }
            else -> {
                try {
                    error?.invoke(e)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    return result
}

/**
 *获取请求体
 */
suspend fun JSONObject.loadRequestBody(): RequestBody {
    val jsonObject = this
    return withContext(Dispatchers.IO) {
        val inGZip = GzipUtils.inGZip(jsonObject.toString().toByteArray())
        val encrypt = try {
            ABDEC.encrypt(inGZip)
        } catch (e: Exception) {
            e.printStackTrace()
            "".toByteArray()
        }
        encrypt.toRequestBody(Pool.HEADER_STREAM_VALUE.toMediaTypeOrNull())
    }
}
