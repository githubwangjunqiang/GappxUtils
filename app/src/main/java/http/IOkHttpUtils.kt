package http

import okhttp3.OkHttpClient
import okhttp3.RequestBody

/**
 * Created by Android-小强 on 2021/11/23.
 * mailbox:980766134@qq.com
 * description: 网络管理接口
 */
interface IOkHttpUtils {
    /**
     * 添加拦截器
     */
    fun addResultInterceptor(interceptor: IResultInterceptor)

    /**
     * 生成一个全局对象
     */
    fun createHttpClient(addTo: ((builder: OkHttpClient.Builder) -> Unit)? = null): OkHttpClient

    /**
     * post 请求接口
     */
    fun postCall(url: String, body: RequestBody): String?

    /**
     * get 请求接口
     */
    fun getCall(url: String, map: Map<String, String>?): String?
}