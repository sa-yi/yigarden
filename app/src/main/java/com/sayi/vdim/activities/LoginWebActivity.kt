package com.sayi.vdim.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.sayi.MainApplication
import com.sayi.vdim.DzConsts
import com.sayi.vdim.databinding.ActivityWebLoginBinding

class WebLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebLoginBinding
    private var cookie: MutableMap<String, String>? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.web.loadUrl("https://i.lty.fan/plugin.php?id=one_sms&action=login&mobile=2")
        binding.web.settings.javaScriptEnabled = true
        binding.web.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.refresh.isEnabled=false
            }
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.refresh.isEnabled=true
                // 页面加载完成后获取Cookie
                val newCookieString: String = CookieManager.getInstance().getCookie(url)
                val newCookieMap: MutableMap<String, String> = extractCookies(newCookieString)

                // 如果cookie未初始化，则直接把newCookieMap的值给它
                if (cookie == null) {
                    cookie = newCookieMap
                    return
                }
                // 对比新旧Cookie的值
                if (cookie != newCookieMap) {
                    Log.v("new Cookie", "Cookies have changed")
                    // 打印新旧Cookie的差异
                    printCookieDifferences(cookie!!, newCookieMap)
                    cookie = newCookieMap


                    if("H65N_2132_auth" in cookie!!){
                        binding.web.webViewClient=WebViewClient()


                        val sharedPreference=getSharedPreferences(DzConsts.COOKIE_PREFS, MODE_PRIVATE)
                        val editor=sharedPreference.edit()
                        editor.putString(DzConsts.COOKIE,newCookieString)
                        editor.apply()
                        MainApplication.toast("登录成功，正在跳转")
                        intent=Intent(this@WebLoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }
        }
        binding.refresh.setOnClickListener{
            binding.web.reload()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.web.stopLoading()
        binding.web.clearCache(true)
        binding.root.removeAllViews()
    }
    private lateinit var intent: Intent

    private fun printCookieDifferences(oldCookies: MutableMap<String, String>, newCookies: MutableMap<String, String>) {
        oldCookies.forEach { (key, value) ->
            if (value != newCookies[key]) {
                Log.d("CookieDifference", "Key: $key, Old Value:$value, New Value: ${newCookies[key]}")
            }
        }
        newCookies.forEach { (key, value) ->
            if (!oldCookies.containsKey(key)) {
                Log.d("CookieDifference", "New Key: $key, Value:$value")
            }
        }
    }
}

fun extractCookies(cookieString: String): MutableMap<String, String> {
    val cookiePattern = Regex("([^=;]+)=([^;]*);?")
    val cookies = mutableMapOf<String, String>()

    cookiePattern.findAll(cookieString).forEach { matchResult ->
        val (key, value) = matchResult.destructured
        cookies[key.trim()] = value.trim()
    }
    Log.d("cookie",cookies.toString())
    return cookies
}
