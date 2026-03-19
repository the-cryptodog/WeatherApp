package com.example.feature.weather.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.core.data.model.DayForecast
import com.example.feature.weather.viewmodel.WeatherUiState
import com.example.feature.weather.viewmodel.WeatherViewModel

@Composable
fun WeekScreen( viewModel: WeatherViewModel) {
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()
    val city     by viewModel.selectedCity.collectAsStateWithLifecycle()

    // city 改變時自動重新載入
    LaunchedEffect(city) {
        viewModel.loadWeather()
    }

    when (val state = uiState) {
        is WeatherUiState.Loading ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        is WeatherUiState.Success ->
            LazyColumn(
                modifier        = Modifier.fillMaxSize(),
                contentPadding  = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.weather.weekForecast) { day ->
                    DayForecastCard(day)
                }
            }
        is WeatherUiState.Error ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                ErrorView(message = state.message, onRetry = viewModel::loadWeather)
            }
    }
}

@Composable
private fun DayForecastCard(day: DayForecast) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = day.date,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text  = day.weatherCode.toDescription(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text  = "${"%.0f".format(day.minTemp)}° / ${"%.0f".format(day.maxTemp)}°",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayForecastCardPreview() {
    MaterialTheme {
        DayForecastCard(
            day = DayForecast(
                date        = "2024-01-01",
                maxTemp     = 28.0,
                minTemp     = 18.0,
                weatherCode = 0
            )
        )
    }
}