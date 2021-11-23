package http.utils

import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import okhttp3.Cookie

/**
 * Created by Android-小强 on 2021/5/11.
 * mailbox:980766134@qq.com
 * description: 自定义缓存 cookie
 */
class DataStoreCookiePersistor : CookiePersistor {

    var listCook = mutableListOf<Cookie>()

    override fun loadAll(): MutableList<Cookie> {
        return listCook
    }

    override fun saveAll(cookies: MutableCollection<Cookie>?) {
        listCook.clear()
        cookies?.let {
            listCook.addAll(it)
        }

    }

    override fun removeAll(cookies: MutableCollection<Cookie>?) {
        cookies?.run {
            listCook.removeAll(this)
        }

    }

    override fun clear() {
        listCook.clear()
    }
}