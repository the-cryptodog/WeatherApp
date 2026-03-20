package com.example.core.data.repository

import com.example.core.data.model.City
import com.example.core.data.model.Weather

// Repository 是 Domain 層跟 Data 層之間的合約
// ViewModel 只知道這個 interface，不知道資料從哪來
// 好處：之後要換 API、加快取、改資料庫，都不需要動 ViewModel
// 這就是 Clean Architecture 的核心：依賴抽象，不依賴實作
interface WeatherRepository {

    // suspend fun 代表這是耗時操作，必須在協程裡呼叫
    // 回傳 Result<T> 而不是直接丟 Exception
    // 讓呼叫端（ViewModel）用 onSuccess/onFailure 處理，更乾淨
    suspend fun getWeather(lat: Double, lon: Double): Result<Weather>

    // 城市清單不是耗時操作（寫死在程式碼裡）
    // 所以不需要 suspend，直接回傳
    fun getCities(): List<City>
}