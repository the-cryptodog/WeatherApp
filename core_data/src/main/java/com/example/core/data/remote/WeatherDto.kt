package com.example.core.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDto(
    @Json(name = "current") val current: CurrentDto,
    @Json(name = "daily")   val daily: DailyDto

)

@JsonClass(generateAdapter = true)
data class CurrentDto(
    @Json(name = "temperature_2m") val temperature: Double,
    @Json(name = "weathercode")    val weatherCode: Int,
    @Json(name = "windspeed_10m")  val windSpeed: Double,
    @Json(name = "apparent_temperature")    val apparentTemperature: Double,
    @Json(name = "precipitation")           val precipitation: Double,
)

@JsonClass(generateAdapter = true)
data class DailyDto(
    @Json(name = "time")                val dates: List<String>,
    @Json(name = "temperature_2m_max")  val maxTemps: List<Double>,
    @Json(name = "temperature_2m_min")  val minTemps: List<Double>,
    @Json(name = "weathercode")         val weatherCodes: List<Int>,
    @Json(name = "uv_index_max")            val uvIndexMax: List<Double>,
    @Json(name = "sunrise")                 val sunrise: List<String>,
    @Json(name = "sunset")                  val sunset: List<String>,
)