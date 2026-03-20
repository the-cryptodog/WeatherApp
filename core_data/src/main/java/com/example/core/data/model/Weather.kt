package com.example.core.data.model

data class Weather(
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val weekForecast: List<DayForecast>,

    // 今日天氣細節
    val apparentTemperature: Double,  // 體感溫度
    val precipitation: Double,        // 降雨量
    val uvIndex: Double,              // UV 指數
    val sunrise: String,              // 日出時間
    val sunset: String,               // 日落時間
)