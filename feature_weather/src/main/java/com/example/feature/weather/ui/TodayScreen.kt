package com.example.feature.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.model.DayForecast
import com.example.core.data.model.Weather
import com.example.feature.weather.ui.theme.WeatherColors
import com.example.feature.weather.viewmodel.WeatherUiState
import com.example.feature.weather.viewmodel.WeatherViewModel
import com.example.weatherapp.feature.weather.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val city    by viewModel.selectedCity.collectAsStateWithLifecycle()

    LaunchedEffect(city) {
        viewModel.loadWeather()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherColors.Background)
    ) {
        // 置中城市名稱
        TopAppBar(
            title = {
                Box(
                    modifier         = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text       = city.name,
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = WeatherColors.TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = WeatherColors.Background
            )
        )

        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading ->
                    CircularProgressIndicator(color = WeatherColors.Primary)
                is WeatherUiState.Success ->
                    TodayContent(weather = state.weather)
                is WeatherUiState.Error ->
                    ErrorView(
                        message = state.message,
                        onRetry = viewModel::loadWeather
                    )
            }
        }
    }
}

@Composable
private fun TodayContent(weather: Weather) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // 大 Emoji
        Text(
            text     = weather.weatherCode.toEmoji(),
            fontSize = 80.sp
        )

        Spacer(Modifier.height(12.dp))

        // 大溫度
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text       = weather.temperature.toInt().toString(),
                fontSize   = 72.sp,
                fontWeight = FontWeight.Light,
                color      = WeatherColors.TextPrimary,
                letterSpacing = (-2).sp
            )
            Text(
                text       = "°C",
                fontSize   = 32.sp,
                fontWeight = FontWeight.Light,
                color      = WeatherColors.TextMuted,
                modifier   = Modifier.padding(top = 10.dp)
            )
        }

        // 天氣描述
        Text(
            text          = weather.weatherCode.toDescription(),
            fontSize      = 16.sp,
            color         = WeatherColors.TextSecondary,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(10.dp))

        // 最高最低溫
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(
                text = "最高  ${weather.weekForecast.firstOrNull()?.maxTemp?.toInt() ?: "--"}°",
                fontSize = 16.sp,
                color    = WeatherColors.TextMuted
            )
            Text(
                text = "最低  ${weather.weekForecast.firstOrNull()?.minTemp?.toInt() ?: "--"}°",
                fontSize = 16.sp,
                color    = WeatherColors.TextMuted
            )
        }

        Spacer(Modifier.height(24.dp))

        // 分隔線
        HorizontalDivider(
            modifier  = Modifier.padding(horizontal = 20.dp),
            thickness = 0.5.dp,
            color     = WeatherColors.DividerToday
        )

        Spacer(Modifier.height(16.dp))

        // 三格資訊：風速、降雨量、UV
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            InfoCell(
                label = stringResource(R.string.today_wind_speed),
                value = weather.windSpeed.toInt().toString(),
                unit  = stringResource(R.string.unit_kmh),
                modifier = Modifier.weight(1f)
            )
            InfoCell(
                label = stringResource(R.string.today_precipitation),
                value = weather.precipitation.toInt().toString(),
                unit  = stringResource(R.string.unit_mm),
                modifier = Modifier.weight(1f)
            )
            InfoCell(
                label = stringResource(R.string.today_uv_index),
                value = weather.uvIndex.toInt().toString(),
                unit  = uvLevel(weather.uvIndex),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        // 三張小卡片：體感、日出、日落
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExtraCard(
                emoji = "🌡️",
                label = stringResource(R.string.today_feels_like),
                value = "${weather.apparentTemperature.toInt()}°C",
                modifier = Modifier.weight(1f)
            )
            ExtraCard(
                emoji = "🌅",
                label = stringResource(R.string.today_sunrise),
                value = weather.sunrise,
                modifier = Modifier.weight(1f)
            )
            ExtraCard(
                emoji = "🌇",
                label = stringResource(R.string.today_sunset),
                value = weather.sunset,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun InfoCell(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text          = label,
            fontSize      = 16.sp,
            color         = WeatherColors.TextMuted,
            letterSpacing = 0.8.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text       = value,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Medium,
            color      = WeatherColors.TextPrimary
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text     = unit,
            fontSize = 14.sp,
            color    = WeatherColors.TextSecondary
        )
    }
}

@Composable
private fun ExtraCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(
            containerColor = WeatherColors.CardBgToday
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = WeatherColors.CardBorder
        )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text          = label,
                fontSize      = 16.sp,
                color         = WeatherColors.TextMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text       = value,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Medium,
                color      = WeatherColors.TextPrimary
            )
        }
    }
}

// UV 指數轉文字描述
private fun uvLevel(uv: Double): String = when {
    uv < 3  -> "低"
    uv < 6  -> "中等"
    uv < 8  -> "高"
    uv < 11 -> "很高"
    else    -> "極高"
}

fun Int.toEmoji(): String = when (this) {
    0         -> "☀️"
    in 1..3   -> "⛅"
    in 45..48 -> "🌫️"
    in 51..67 -> "🌦️"
    in 71..77 -> "❄️"
    in 80..82 -> "🌧️"
    in 95..99 -> "⛈️"
    else      -> "🌡️"
}

fun Int.toDescription(): String = when (this) {
    0         -> "晴天"
    in 1..3   -> "多雲"
    in 45..48 -> "霧"
    in 51..67 -> "毛毛雨"
    in 71..77 -> "下雪"
    in 80..82 -> "陣雨"
    in 95..99 -> "雷雨"
    else      -> "未知"
}

@Preview(showBackground = true, backgroundColor = 0xFF090E18)
@Composable
fun TodayContentPreview() {
    MaterialTheme {
        TodayContent(
            weather = Weather(
                temperature         = 25.0,
                weatherCode         = 0,
                windSpeed           = 12.0,
                apparentTemperature = 23.0,
                precipitation       = 0.0,
                uvIndex             = 6.0,
                sunrise             = "06:22",
                sunset              = "17:45",
                weekForecast        = listOf(
                    DayForecast("2024-01-01", 28.0, 19.0, 0)
                )
            )
        )
    }
}