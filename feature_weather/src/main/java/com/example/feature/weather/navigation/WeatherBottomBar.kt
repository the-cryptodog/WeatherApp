package com.example.feature.weather.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun WeatherBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "today",
            onClick  = { navController.navigateSingleTop("today") },
            icon     = { Icon(Icons.Default.WbSunny, contentDescription = null) },
            label    = { Text("今天") }
        )
        NavigationBarItem(
            selected = currentRoute == "week",
            onClick  = { navController.navigateSingleTop("week") },
            icon     = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label    = { Text("本週") }
        )
        NavigationBarItem(
            selected = currentRoute == "cities",
            onClick  = { navController.navigateSingleTop("cities") },
            icon     = { Icon(Icons.Default.LocationCity, contentDescription = null) },
            label    = { Text("城市") }
        )
    }
}

fun NavController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState    = true
        popUpTo(graph.findStartDestination().id) { saveState = true }
    }
}