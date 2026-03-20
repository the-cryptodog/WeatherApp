package com.example.core.data.repository

import com.example.core.data.di.IoDispatcher
import com.example.core.data.model.City
import com.example.core.data.model.Weather
import com.example.core.data.remote.OpenMeteoApiService
import com.example.core.data.remote.toWeather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApiService,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<Weather> =
    // withContext 明確指定這段程式碼在 IO 執行緒執行
        // 網路請求絕對不能在 Main Thread 執行，否則會 ANR
        withContext(dispatcher) {
            try {
                val response = api.getForecast(lat, lon)
                Result.success(response.toWeather())
            } catch (e: IOException) {
                // IOException = 網路層問題
                // 例如：沒有網路、DNS 解析失敗、連線逾時
                Result.failure(Exception("網路連線失敗，請檢查網路設定"))
            } catch (e: HttpException) {
                // HttpException = 伺服器有回應，但狀態碼不是 2xx
                // 例如：404 找不到、500 伺服器錯誤
                Result.failure(Exception("伺服器錯誤（${e.code()}），請稍後再試"))
            } catch (e: Exception) {
                // 其他未預期的錯誤
                // 例如：JSON 解析失敗、資料格式不符
                Result.failure(Exception("發生未知錯誤：${e.message}"))
            }
        }


    override fun getCities(): List<City> = listOf(
        // 亞洲
        City("台北",    25.04,   121.53  ,"亞洲"),
        City("台中",    24.15,   120.67  ,"亞洲"),
        City("高雄",    22.63,   120.30  ,"亞洲"),
        City("東京",    35.68,   139.69  ,"亞洲"),
        City("大阪",    34.69,   135.50  ,"亞洲"),
        City("首爾",    37.57,   126.98  ,"亞洲"),
        City("北京",    39.90,   116.40  ,"亞洲"),
        City("上海",    31.23,   121.47  ,"亞洲"),
        City("香港",    22.32,   114.17  ,"亞洲"),
        City("新加坡",   1.35,   103.82  ,"亞洲"),
        City("曼谷",    13.75,   100.52  ,"亞洲"),
        City("吉隆坡",   3.14,   101.69  ,"亞洲"),
        City("雅加達",  -6.21,   106.85  ,"亞洲"),
        City("馬尼拉",  14.60,   120.98  ,"亞洲"),
        City("河內",    21.03,   105.85  ,"亞洲"),
        City("孟買",    19.08,    72.88  ,"亞洲"),
        City("新德里",  28.61,    77.21  ,"亞洲"),
        City("杜拜",    25.20,    55.27  ,"亞洲"),
        City("東京",    35.68,   139.69  ,"亞洲"),

        // 歐洲
        City("倫敦",    51.51,    -0.13  ,"歐洲"),
        City("巴黎",    48.85,     2.35,"歐洲"),
        City("柏林",    52.52,    13.40,"歐洲"),
        City("羅馬",    41.90,    12.50,"歐洲"),
        City("馬德里",  40.42,    -3.70,"歐洲"),
        City("阿姆斯特丹", 52.37,   4.90,"歐洲"),
        City("維也納",  48.21,    16.37,"歐洲"),
        City("蘇黎世",  47.38,     8.54,"歐洲"),
        City("布魯塞爾", 50.85,    4.35,"歐洲"),
        City("斯德哥爾摩", 59.33,  18.07,"歐洲"),

        // 美洲
        City("紐約",    40.71,   -74.01,"美洲"),
        City("洛杉磯",  34.05,  -118.24,"美洲"),
        City("芝加哥",  41.88,   -87.63,"美洲"),
        City("多倫多",  43.65,   -79.38,"美洲"),
        City("溫哥華",  49.25,  -123.12,"美洲"),
        City("墨西哥城", 19.43,  -99.13,"美洲"),
        City("聖保羅", -23.55,   -46.63,"美洲"),
        City("布宜諾斯艾利斯", -34.60, -58.38,"美洲"),

        // 大洋洲
        City("雪梨",   -33.87,   151.21,"大洋洲"),
        City("墨爾本", -37.81,   144.96,"大洋洲"),
        City("奧克蘭", -36.85,   174.76,"大洋洲"),

        // 非洲
        City("開羅",    30.06,    31.25,"非洲"),
        City("奈洛比",  -1.29,    36.82,"非洲"),
        City("約翰尼斯堡", -26.20, 28.04,"非洲"),
    )
}