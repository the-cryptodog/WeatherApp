package com.example.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApiService {

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude")      latitude: Double,
        @Query("longitude")     longitude: Double,
        @Query("current")       current: String = "temperature_2m,weathercode,windspeed_10m",
        @Query("daily")         daily: String   = "temperature_2m_max,temperature_2m_min,weathercode",
        @Query("timezone")      timezone: String = "auto",
        @Query("forecast_days") days: Int        = 7
    ): WeatherDto
}