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
        // 亞洲
        City("台北",    25.04,   121.53),
        City("台中",    24.15,   120.67),
        City("高雄",    22.63,   120.30),
        City("東京",    35.68,   139.69),
        City("大阪",    34.69,   135.50),
        City("首爾",    37.57,   126.98),
        City("北京",    39.90,   116.40),
        City("上海",    31.23,   121.47),
        City("香港",    22.32,   114.17),
        City("新加坡",   1.35,   103.82),
        City("曼谷",    13.75,   100.52),
        City("吉隆坡",   3.14,   101.69),
        City("雅加達",  -6.21,   106.85),
        City("馬尼拉",  14.60,   120.98),
        City("河內",    21.03,   105.85),
        City("孟買",    19.08,    72.88),
        City("新德里",  28.61,    77.21),
        City("杜拜",    25.20,    55.27),
        City("東京",    35.68,   139.69),

        // 歐洲
        City("倫敦",    51.51,    -0.13),
        City("巴黎",    48.85,     2.35),
        City("柏林",    52.52,    13.40),
        City("羅馬",    41.90,    12.50),
        City("馬德里",  40.42,    -3.70),
        City("阿姆斯特丹", 52.37,   4.90),
        City("維也納",  48.21,    16.37),
        City("蘇黎世",  47.38,     8.54),
        City("布魯塞爾", 50.85,    4.35),
        City("斯德哥爾摩", 59.33,  18.07),

        // 美洲
        City("紐約",    40.71,   -74.01),
        City("洛杉磯",  34.05,  -118.24),
        City("芝加哥",  41.88,   -87.63),
        City("多倫多",  43.65,   -79.38),
        City("溫哥華",  49.25,  -123.12),
        City("墨西哥城", 19.43,  -99.13),
        City("聖保羅", -23.55,   -46.63),
        City("布宜諾斯艾利斯", -34.60, -58.38),

        // 大洋洲
        City("雪梨",   -33.87,   151.21),
        City("墨爾本", -37.81,   144.96),
        City("奧克蘭", -36.85,   174.76),

        // 非洲
        City("開羅",    30.06,    31.25),
        City("奈洛比",  -1.29,    36.82),
        City("約翰尼斯堡", -26.20, 28.04),
    )
}