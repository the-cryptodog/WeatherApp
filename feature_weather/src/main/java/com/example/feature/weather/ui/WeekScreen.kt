package com.example.feature.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.model.DayForecast
import com.example.feature.weather.ui.theme.WeatherColors
import com.example.feature.weather.viewmodel.WeatherUiState
import com.example.feature.weather.viewmodel.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekScreen(viewModel: WeatherViewModel) {
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
        TopAppBar(
            title = {
                Text(
                    text       = "本週預報",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = WeatherColors.TextPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = WeatherColors.Background
            )
        )

        when (val state = uiState) {
            is WeatherUiState.Loading ->
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = WeatherColors.Primary)
                }
            is WeatherUiState.Success ->
                LazyColumn(
                    modifier            = Modifier.fillMaxSize(),
                    contentPadding      = PaddingValues(
                        start  = 16.dp,
                        end    = 16.dp,
                        top    = 8.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(state.weather.weekForecast) { index, day ->
                        WeekDayCard(day = day, isToday = index == 0)
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
            containerColor = if (isToday) WeatherColors.CardBgToday else WeatherColors.CardBg
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = if (isToday) WeatherColors.CardBorderToday else WeatherColors.CardBorder
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
                color      = if (isToday) WeatherColors.Primary else WeatherColors.TextSecondary,
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
                color    = WeatherColors.TextMuted,
                modifier = Modifier.weight(1f)
            )

            // 溫度
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = "${day.maxTemp.toInt()}°",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = WeatherColors.TextPrimary
                )
                Text(
                    text     = "  /  ${day.minTemp.toInt()}°",
                    fontSize = 16.sp,
                    color    = WeatherColors.TextMuted
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