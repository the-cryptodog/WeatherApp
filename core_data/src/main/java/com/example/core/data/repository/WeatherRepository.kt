package com.example.core.data.repository

import com.example.core.data.model.City
import com.example.core.data.model.Weather

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Result<Weather>
    fun getCities(): List<City>
}