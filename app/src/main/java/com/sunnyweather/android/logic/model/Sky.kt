package com.sunnyweather.android.logic.model

import com.sunnyweather.android.R

class Sky(
    val info: String,    // 天气描述（如"晴"、"雨"）
    val icon: Int,       // 天气图标资源ID（Int类型）
    val bg: Int          // 背景图资源ID
)

// 映射天气类型与对应的Sky对象（icon使用资源ID）
private val skyMap = mapOf(
    // 晴天
    "CLEAR_DAY" to Sky("晴", R.drawable.ic_clear_day, R.drawable.bg_clear_day),
    "CLEAR_NIGHT" to Sky("晴", R.drawable.ic_clear_night, R.drawable.bg_clear_night),

    // 多云
    "PARTLY_CLOUDY_DAY" to Sky("多云", R.drawable.ic_partly_cloudy_day, R.drawable.bg_partly_cloudy_day),
    "PARTLY_CLOUDY_NIGHT" to Sky("多云", R.drawable.ic_partly_cloudy_night, R.drawable.bg_partly_cloudy_night),

    // 阴天
    "CLOUDY" to Sky("阴", R.drawable.ic_cloudy, R.drawable.bg_cloudy),

    // 雨天
    "RAIN_DAY" to Sky("雨", R.drawable.ic_rain_day, R.drawable.bg_rain_day),
    "RAIN_NIGHT" to Sky("雨", R.drawable.ic_rain_night, R.drawable.bg_rain_night),

    // 雪天
    "SNOW_DAY" to Sky("雪", R.drawable.ic_snow_day, R.drawable.bg_snow_day),
    "SNOW_NIGHT" to Sky("雪", R.drawable.ic_snow_night, R.drawable.bg_snow_night),

    // 雾天
    "FOG" to Sky("雾", R.drawable.ic_fog, R.drawable.bg_fog),

    // 大风
    "WIND" to Sky("大风", R.drawable.ic_wind, R.drawable.bg_wind)
)

// 根据天气类型获取对应的Sky对象（默认返回晴天）
fun getSky(skycon: String): Sky {
    return skyMap[skycon] ?: skyMap["CLEAR_DAY"]!!
}