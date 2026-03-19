package com.example.feature.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.City
import com.example.core.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _cities = MutableStateFlow(repository.getCities())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    private val _selectedCity = MutableStateFlow(repository.getCities().first())
    val selectedCity: StateFlow<City> = _selectedCity.asStateFlow()

    fun onCitySelected(city: City) {
        _selectedCity.value = city
    }

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            repository.getWeather(
                _selectedCity.value.latitude,
                _selectedCity.value.longitude
            )
                .onSuccess { _uiState.value = WeatherUiState.Success(it) }
                .onFailure { _uiState.value = WeatherUiState.Error(it.message ?: "無法取得天氣") }
        }
    }
}