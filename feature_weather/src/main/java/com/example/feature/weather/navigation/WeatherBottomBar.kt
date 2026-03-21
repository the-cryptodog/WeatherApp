package com.example.feature.weather.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.feature.weather.ui.theme.WeatherColors
import com.example.weatherapp.feature.weather.R

@Composable
fun WeatherBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = WeatherColors.Background,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "today",
            onClick  = { navController.navigateSingleTop("today") },
            icon     = {
                NavItemContent(
                    emoji      = "☀️",
                    label      = stringResource(R.string.nav_today),
                    isSelected = currentRoute == "today"
                )
            },
            label  = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "week",
            onClick  = { navController.navigateSingleTop("week") },
            icon     = {
                NavItemContent(
                    emoji      = "📅",
                    label      = stringResource(R.string.nav_week),
                    isSelected = currentRoute == "week"
                )
            },
            label  = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "cities",
            onClick  = { navController.navigateSingleTop("cities") },
            icon     = {
                NavItemContent(
                    emoji      = "🏙",
                    label      = stringResource(R.string.nav_cities),
                    isSelected = currentRoute == "cities"
                )
            },
            label  = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun NavItemContent(
    emoji: String,
    label: String,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = emoji, fontSize = 22.sp)
        Text(
            text          = label,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Medium,
            color         = if (isSelected) WeatherColors.Primary else WeatherColors.TextNavUnselectedBottomBar,
            letterSpacing = 0.3.sp
        )
        // 選中小圓點
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(
                    color = if (isSelected) WeatherColors.Primary else Color.Transparent,
                    shape = CircleShape
                )
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