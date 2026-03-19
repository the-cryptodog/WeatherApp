package com.example.feature.weather.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.model.Weather
import com.example.feature.weather.viewmodel.WeatherUiState
import com.example.feature.weather.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    viewModel: WeatherViewModel,  // 不再自己建立
    onNavigateToCities: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val city    by viewModel.selectedCity.collectAsStateWithLifecycle()

    // city 改變時自動重新載入
    LaunchedEffect(city) {
        viewModel.loadWeather()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(city.name) },
                actions = {
                    IconButton(onClick = onNavigateToCities) {
                        Icon(
                            imageVector        = Icons.Default.LocationCity,
                            contentDescription = "選擇城市"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading ->
                    CircularProgressIndicator()
                is WeatherUiState.Success ->
                    TodayContent(weather = state.weather)
                is WeatherUiState.Error ->
                    ErrorView(message = state.message, onRetry = viewModel::loadWeather)
            }
        }
    }
}

@Composable
private fun TodayContent(weather: Weather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text  = "${"%.1f".format(weather.temperature)}°C",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text  = weather.weatherCode.toDescription(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalDivider()
        Text(
            text  = "風速：${weather.windSpeed} km/h",
            style = MaterialTheme.typography.bodyLarge
        )
    }
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

@Preview(showBackground = true)
@Composable
fun TodayContentPreview() {
    MaterialTheme {
        TodayContent(
            weather = Weather(
                temperature = 25.0,
                weatherCode = 0,
                windSpeed = 12.5,
                weekForecast = emptyList()
            )
        )
    }
}