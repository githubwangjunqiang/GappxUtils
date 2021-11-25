package http

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import http.exception.MyHttpCodeException
import http.utils.CallPool
import http.utils.CallUtils
import http.utils.DataStoreCookiePersistor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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

    val mInterceptors: LinkedList<IResultInterceptor> by lazy {
        LinkedList()
    }

    override fun addResultInterceptor(interceptor: IResultInterceptor) {
        mInterceptors.add(interceptor)
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
        var string: String?
        var client: OkHttpClient = okhttpManager
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val newCall = client.newCall(request)
        val execute = newCall.execute()
        if (execute.code == CallPool.HTTP_OK) {
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
        if (execute.code == CallPool.HTTP_OK) {
            string = execute.body?.string()
        } else {
            throw MyHttpCodeException(execute.code, execute.message)
        }
        return string
    }


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
        result = CallUtils.loadResult(url, body, clazz)
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
        val inGZip = CallUtils.zip(jsonObject.toString().toByteArray())
        val encrypt = CallUtils.aesData(inGZip)
        encrypt.toRequestBody(CallPool.HEADER_STREAM_VALUE.toMediaTypeOrNull())
    }
}


