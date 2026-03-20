package com.example.feature.weather.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.model.DayForecast
import com.example.core.data.model.Weather
import com.example.feature.weather.viewmodel.WeatherUiState
import com.example.feature.weather.viewmodel.WeatherViewModel

// 日系深藍配色常數，集中管理方便之後修改
private val ColorBackground    = Color(0xFF090E18)
private val ColorPrimary       = Color(0xFF90C0F0)   // 強調色：鋼藍
private val ColorTextPrimary   = Color(0xFFEEF4FF)   // 主要文字：冷藍白
private val ColorTextSecondary = Color(0xFF8AA0C0)   // 次要文字
private val ColorTextMuted     = Color(0xFF6080A0)   // 最淡文字（label）
private val ColorCardBg        = Color(0x14508CDC)   // 卡片背景（半透明藍）
private val ColorCardBorder    = Color(0x33508CDC)   // 卡片邊框
private val ColorDivider       = Color(0x33508CDC)   // 分隔線

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    viewModel: WeatherViewModel,
    onNavigateToCities: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val city    by viewModel.selectedCity.collectAsStateWithLifecycle()

    LaunchedEffect(city) {
        viewModel.loadWeather()
    }

    Scaffold(
        containerColor = ColorBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = city.name,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = ColorTextPrimary
                    )
                },
                actions = {
                    TextButton(onClick = onNavigateToCities) {
                        Icon(
                            imageVector        = Icons.Default.LocationCity,
                            contentDescription = null,
                            tint               = ColorPrimary,
                            modifier           = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text     = "切換城市",
                            fontSize = 12.sp,
                            color    = ColorPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorBackground
                )
            )
        }
    ) { padding ->
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading ->
                    CircularProgressIndicator(color = ColorPrimary)
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
                color      = ColorTextPrimary,
                letterSpacing = (-2).sp
            )
            Text(
                text       = "°C",
                fontSize   = 32.sp,
                fontWeight = FontWeight.Light,
                color      = ColorTextMuted,
                modifier   = Modifier.padding(top = 10.dp)
            )
        }

        // 天氣描述
        Text(
            text          = weather.weatherCode.toDescription(),
            fontSize      = 16.sp,
            color         = ColorTextSecondary,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(10.dp))

        // 最高最低溫
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(
                text = "最高  ${weather.weekForecast.firstOrNull()?.maxTemp?.toInt() ?: "--"}°",
                fontSize = 16.sp,
                color    = ColorTextMuted
            )
            Text(
                text = "最低  ${weather.weekForecast.firstOrNull()?.minTemp?.toInt() ?: "--"}°",
                fontSize = 16.sp,
                color    = ColorTextMuted
            )
        }

        Spacer(Modifier.height(24.dp))

        // 分隔線
        HorizontalDivider(
            modifier  = Modifier.padding(horizontal = 20.dp),
            thickness = 0.5.dp,
            color     = ColorDivider
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
                label = "風　速",
                value = weather.windSpeed.toInt().toString(),
                unit  = "km/h",
                modifier = Modifier.weight(1f)
            )
            InfoCell(
                label = "降雨量",
                value = weather.precipitation.toInt().toString(),
                unit  = "mm",
                modifier = Modifier.weight(1f)
            )
            InfoCell(
                label = "UV 指數",
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
                label = "體　感",
                value = "${weather.apparentTemperature.toInt()}°C",
                modifier = Modifier.weight(1f)
            )
            ExtraCard(
                emoji = "🌅",
                label = "日　出",
                value = weather.sunrise,
                modifier = Modifier.weight(1f)
            )
            ExtraCard(
                emoji = "🌇",
                label = "日　落",
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
            color         = ColorTextMuted,
            letterSpacing = 0.8.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text       = value,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Medium,
            color      = ColorTextPrimary
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text     = unit,
            fontSize = 14.sp,
            color    = ColorTextSecondary
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
            containerColor = ColorCardBg
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = ColorCardBorder
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
                color         = ColorTextMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text       = value,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Medium,
                color      = ColorPrimary
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