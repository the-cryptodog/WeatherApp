package com.example.core.data.remote

import com.example.core.data.model.Weather
import com.example.core.data.model.DayForecast

fun WeatherDto.toWeather() = Weather(
    temperature = current.temperature,
    weatherCode = current.weatherCode,
    windSpeed = current.windSpeed,
    weekForecast = daily.dates.mapIndexed { i, date ->
        DayForecast(
            date = date,
            maxTemp = daily.maxTemps[i],
            minTemp = daily.minTemps[i],
            weatherCode = daily.weatherCodes[i]
        )
    }
)