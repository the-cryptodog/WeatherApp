package com.example.feature.weather.viewmodel

import com.example.core.data.model.Weather

// sealed interface 的好處：
// when 表達式必須窮舉所有狀態，編譯器會檢查
// 如果之後新增一個狀態，所有用到 when 的地方都會報錯提醒你處理
// 比 enum 更靈活，每個狀態可以帶不同的資料
sealed interface WeatherUiState {

    // 載入中：不帶任何資料
    // data object 而不是 object，是為了讓 equals/toString 正確運作
    data object Loading : WeatherUiState

    // 成功：帶著天氣資料
    data class Success(val weather: Weather) : WeatherUiState

    // 失敗：帶著錯誤訊息
    // 訊息已在 Repository 層轉成使用者看得懂的文字
    data class Error(val message: String) : WeatherUiState
}