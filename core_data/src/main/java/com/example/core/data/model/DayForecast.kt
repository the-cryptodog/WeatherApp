package com.example.core.data.model

data class DayForecast(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val weatherCode: Int
)