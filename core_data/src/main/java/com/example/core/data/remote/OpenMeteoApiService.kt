package com.example.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit 會根據這個 interface 自動產生實作
// suspend fun 讓 Retrofit 在協程環境下執行網路請求
// Retrofit + OkHttp 會自動切換到 IO 執行緒，不需要手動 withContext
interface OpenMeteoApiService {

    // @GET 指定這是 GET 請求，路徑是 v1/forecast
    // 完整 URL = baseUrl + 路徑 = https://api.open-meteo.com/v1/forecast
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude")      latitude: Double,
        @Query("longitude")     longitude: Double,
        // current 指定要回傳哪些即時天氣欄位
        @Query("current")       current: String  = "temperature_2m,weathercode,windspeed_10m,apparent_temperature,precipitation",
        // daily 指定要回傳哪些每日預報欄位
        @Query("daily")         daily: String    = "temperature_2m_max,temperature_2m_min,weathercode,uv_index_max,sunrise,sunset",
        // auto 讓 API 根據經緯度自動判斷時區
        @Query("timezone")      timezone: String = "auto",
        @Query("forecast_days") days: Int        = 7
    ): WeatherDto
}