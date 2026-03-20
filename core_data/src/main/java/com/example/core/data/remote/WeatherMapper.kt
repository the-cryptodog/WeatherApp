package com.example.core.data.remote

import com.example.core.data.model.DayForecast
import com.example.core.data.model.Weather

// Mapper 的職責：把 API 回傳的 DTO 轉換成 Domain Model
// 為什麼要分開？
// DTO 的結構跟著 API 走，API 改了 DTO 就要跟著改
// Domain Model 的結構跟著業務邏輯走，保持穩定
// 這樣 API 改變時，只需要改 Mapper，不需要動 ViewModel 或 Screen
fun WeatherDto.toWeather() = Weather(
    temperature  = current.temperature,
    weatherCode  = current.weatherCode,
    windSpeed    = current.windSpeed,
    apparentTemperature  = current.apparentTemperature,
    precipitation        = current.precipitation,
    uvIndex              = daily.uvIndexMax.firstOrNull() ?: 0.0,
    // 日出日落只取時間部分，API 回傳格式是 "2024-01-01T06:22"
    sunrise              = daily.sunrise.firstOrNull()?.takeLast(5) ?: "--:--",
    sunset               = daily.sunset.firstOrNull()?.takeLast(5) ?: "--:--",
    weekForecast = daily.dates.mapIndexed { i, date ->
        DayForecast(
            date        = date,
            maxTemp     = daily.maxTemps[i],
            minTemp     = daily.minTemps[i],
            weatherCode = daily.weatherCodes[i]
        )
    }
)