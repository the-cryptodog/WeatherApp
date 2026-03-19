package com.example.core.data.model

data class Weather(
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val weekForecast: List<DayForecast>
)