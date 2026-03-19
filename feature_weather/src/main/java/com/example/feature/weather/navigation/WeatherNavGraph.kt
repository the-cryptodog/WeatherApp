package com.example.feature.weather.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.weather.ui.CityListScreen
import com.example.feature.weather.ui.TodayScreen
import com.example.feature.weather.ui.WeekScreen
import com.example.feature.weather.viewmodel.WeatherViewModel

@Composable
fun WeatherNavGraph() {
    val navController = rememberNavController()

    // 在這裡建立唯一的 ViewModel 實例，所有 Screen 共用
    val viewModel: WeatherViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { WeatherBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = "today"
        ) {
            composable("today") {
                TodayScreen(
                    viewModel          = viewModel,
                    onNavigateToCities = {
                        navController.navigate("cities")
                    }
                )
            }
            composable("week") {
                WeekScreen(viewModel = viewModel)
            }
            composable("cities") {
                CityListScreen(
                    viewModel      = viewModel,
                    onCitySelected = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}