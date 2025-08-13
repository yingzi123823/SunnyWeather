package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {// 单例,全局获取Context
    companion object{// 静态成员变量，伴生对象
        const val TOKEN = "4i3Yx4aJ1EZ6TysN"
        @SuppressLint("StaticFieldLeak")// 忽略警告
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}