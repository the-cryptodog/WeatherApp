package com.example.feature.weather.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.weather.ui.CityListScreen
import com.example.feature.weather.ui.TodayScreen
import com.example.feature.weather.ui.WeekScreen
import com.example.feature.weather.ui.theme.WeatherColors
import com.example.feature.weather.viewmodel.WeatherViewModel

@Composable
fun WeatherNavGraph() {
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = hiltViewModel()

    // 只管 BottomBar，不管 TopAppBar
    Scaffold(
        containerColor = WeatherColors.Background,
        bottomBar      = { WeatherBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = "today",
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable("today") {
                TodayScreen(viewModel = viewModel)
            }
            composable("week") {
                WeekScreen(viewModel = viewModel)
            }
            composable("cities") {
                CityListScreen(
                    viewModel      = viewModel,
                    onCitySelected = { navController.popBackStack() }
                )
            }
        }
    }
}