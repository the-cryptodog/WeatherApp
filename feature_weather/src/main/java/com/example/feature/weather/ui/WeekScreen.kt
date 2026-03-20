package com.example.feature.weather.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.feature.weather.viewmodel.WeatherUiState
import com.example.feature.weather.viewmodel.WeatherViewModel

// 沿用 TodayScreen 的配色常數保持整體一致
private val ColorBackground    = Color(0xFF090E18)
private val ColorPrimary       = Color(0xFF90C0F0)
private val ColorTextPrimary   = Color(0xFFEEF4FF)
private val ColorTextSecondary = Color(0xFF8AA0C0)
private val ColorTextMuted     = Color(0xFF6080A0)
private val ColorCardBg        = Color(0xFF0F1825)
private val ColorCardBgToday   = Color(0xFF0F1E35)
private val ColorCardBorder    = Color(0x1A508CDC)
private val ColorCardBorderToday = Color(0x66508CDC)

@Composable
fun WeekScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val city    by viewModel.selectedCity.collectAsStateWithLifecycle()

    LaunchedEffect(city) {
        viewModel.loadWeather()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = ColorBackground
    ) {
        when (val state = uiState) {
            is WeatherUiState.Loading ->
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = ColorPrimary)
                }

            is WeatherUiState.Success ->
                LazyColumn(
                    modifier        = Modifier.fillMaxSize(),
                    contentPadding  = PaddingValues(
                        start  = 16.dp,
                        end    = 16.dp,
                        top    = 20.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text       = "本週預報",
                            fontSize   = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = ColorTextPrimary,
                            modifier   = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    itemsIndexed(state.weather.weekForecast) { index, day ->
                        WeekDayCard(
                            day     = day,
                            isToday = index == 0
                        )
                    }
                }

            is WeatherUiState.Error ->
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    ErrorView(
                        message = state.message,
                        onRetry = viewModel::loadWeather
                    )
                }
        }
    }
}

@Composable
private fun WeekDayCard(
    day: DayForecast,
    isToday: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(
            containerColor = if (isToday) ColorCardBgToday else ColorCardBg
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = if (isToday) ColorCardBorderToday else ColorCardBorder
        )
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 日期欄
            Text(
                text       = if (isToday) "今天" else day.date.takeLast(5),
                fontSize   = 16.sp,
                fontWeight = FontWeight.Medium,
                color      = if (isToday) ColorPrimary else ColorTextSecondary,
                modifier   = Modifier.width(52.dp)
            )

            // Emoji
            Text(
                text     = day.weatherCode.toEmoji(),
                fontSize = 22.sp,
                modifier = Modifier.width(36.dp)
            )

            // 天氣描述
            Text(
                text     = day.weatherCode.toDescription(),
                fontSize = 16.sp,
                color    = ColorTextMuted,
                modifier = Modifier.weight(1f)
            )

            // 溫度
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = "${day.maxTemp.toInt()}°",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = ColorTextPrimary
                )
                Text(
                    text     = "  /  ${day.minTemp.toInt()}°",
                    fontSize = 16.sp,
                    color    = ColorTextMuted
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090E18)
@Composable
fun WeekDayCardPreview() {
    MaterialTheme {
        Column(
            modifier            = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeekDayCard(
                day     = DayForecast("2024-01-01", 28.0, 19.0, 0),
                isToday = true
            )
            WeekDayCard(
                day     = DayForecast("2024-01-02", 24.0, 17.0, 2),
                isToday = false
            )
            WeekDayCard(
                day     = DayForecast("2024-01-03", 21.0, 16.0, 80),
                isToday = false
            )
        }
    }
}