package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"// 彩云天气的接口
    private val retrofit = Retrofit.Builder()// 创建Retrofit对象
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)// 创建一个服务接口实例
    inline fun <reified T> create(): T = create(T::class.java)// 内联函数, 将创建对象过程内联进调用者处
}