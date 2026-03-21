package com.example.feature.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.model.City
import com.example.feature.weather.ui.theme.WeatherColors
import com.example.feature.weather.viewmodel.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    viewModel: WeatherViewModel,
    onCitySelected: () -> Unit
) {
    val cities       by viewModel.cities.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()

    val groupedCities = cities.groupBy { it.region }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherColors.Background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text       = "選擇城市",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = WeatherColors.TextPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = WeatherColors.Background
            )
        )

        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start  = 16.dp,
                end    = 16.dp,
                top    = 8.dp,
                bottom = 16.dp
            )
        ) {
            groupedCities.forEach { (region, citiesInRegion) ->
                item {
                    Text(
                        text          = region,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = WeatherColors.RegionLabel,
                        letterSpacing = 1.5.sp,
                        modifier      = Modifier.padding(
                            top    = 16.dp,
                            bottom = 6.dp,
                            start  = 4.dp
                        )
                    )
                }
                items(citiesInRegion, key = { it.name }) { city ->
                    CityItem(
                        city       = city,
                        isSelected = city.name == selectedCity.name,
                        onClick    = {
                            viewModel.onCitySelected(city)
                            onCitySelected()
                        }
                    )
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun CityItem(
    city: City,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(
            containerColor = if (isSelected) WeatherColors.CardBgSelected else WeatherColors.CardBg
        ),
        border   = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = if (isSelected) WeatherColors.CardBorderSelected else WeatherColors.CardBorder
        )
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = city.name,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color      = if (isSelected) WeatherColors.TextPrimary else WeatherColors.TextSecondary
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = "${city.latitude}°N  ${city.longitude}°E",
                    fontSize = 11.sp,
                    color    = WeatherColors.TextMuted
                )
            }
            if (isSelected) {
                Icon(
                    imageVector        = Icons.Default.Check,
                    contentDescription = null,
                    tint               = WeatherColors.Primary,
                    modifier           = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090E18)
@Composable
fun CityItemPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color(0xFF090E18)
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CityItem(
                city       = City("台北", 25.04, 121.53, "亞洲"),
                isSelected = true,
                onClick    = {}
            )
            CityItem(
                city       = City("東京", 35.68, 139.69, "亞洲"),
                isSelected = false,
                onClick    = {}
            )
        }
    }
}