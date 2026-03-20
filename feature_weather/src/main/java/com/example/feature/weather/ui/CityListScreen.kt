package com.example.feature.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
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
import com.example.feature.weather.viewmodel.WeatherViewModel

private val ColorBackground      = Color(0xFF090E18)
private val ColorPrimary         = Color(0xFF90C0F0)
private val ColorTextPrimary     = Color(0xFFEEF4FF)
private val ColorTextSecondary   = Color(0xFFC0D4E8)
private val ColorTextMuted       = Color(0xFF6080A0)
private val ColorCardBg          = Color(0xFF0F1825)
private val ColorCardBgSelected  = Color(0xFF0F1E35)
private val ColorCardBorder      = Color(0x1A508CDC)
private val ColorCardBorderSelected = Color(0x66508CDC)
private val ColorRegionLabel     = Color(0xFF6080A0)

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
            .background(ColorBackground)
    ) {
        TopAppBar(
            title = {
                Text(
                    text       = "選擇城市",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = ColorTextPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ColorBackground
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
                        color         = ColorRegionLabel,
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
            containerColor = if (isSelected) ColorCardBgSelected else ColorCardBg
        ),
        border   = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = if (isSelected) ColorCardBorderSelected else ColorCardBorder
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
                    color      = if (isSelected) ColorPrimary else ColorTextSecondary
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = "${city.latitude}°N  ${city.longitude}°E",
                    fontSize = 11.sp,
                    color    = ColorTextMuted
                )
            }
            if (isSelected) {
                Icon(
                    imageVector        = Icons.Default.Check,
                    contentDescription = null,
                    tint               = ColorPrimary,
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