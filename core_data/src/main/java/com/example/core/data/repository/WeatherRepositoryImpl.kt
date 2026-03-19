package com.example.core.data.repository


import com.example.core.data.model.City
import com.example.core.data.model.Weather
import com.example.core.data.remote.OpenMeteoApiService
import com.example.core.data.remote.toWeather
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApiService
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<Weather> =
        runCatching { api.getForecast(lat, lon).toWeather() }

    override fun getCities(): List<City> = listOf(
        City("台北", 25.04, 121.53),
        City("東京",  35.68,  139.69),
        City("首爾",  37.57,  126.98),
        City("倫敦",  51.51,   -0.13),
        City("紐約",  40.71,  -74.01),
        City("雪梨", -33.87,  151.21),
        City("新加坡",  1.35,  103.82),
        City("巴黎",  48.85,    2.35),
    )
}