package com.example.feature.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.City
import com.example.core.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    // ── StateFlow 設計原則 ──
    // 私有 MutableStateFlow：只有 ViewModel 自己可以修改
    // 公開 StateFlow：外部（Screen）只能讀取，不能修改
    // 這樣確保資料流向是單向的：ViewModel → View
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _cities = MutableStateFlow(repository.getCities())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    private val _selectedCity = MutableStateFlow(repository.getCities().first())
    val selectedCity: StateFlow<City> = _selectedCity.asStateFlow()

    // 用來追蹤當前載入的 Job
    // 目的：使用者快速切換城市時，取消上一個還沒完成的請求
    // 避免多個協程同時跑，最後誰先完成就顯示誰的結果（Race Condition）
    private var loadJob: Job? = null

    // ── 對外事件入口（View 呼叫這些 function）──

    // 切換城市
    // 只負責更新 selectedCity，載入天氣由 loadWeather 負責
    fun onCitySelected(city: City) {
        _selectedCity.value = city
    }

    // 載入天氣資料
    // 同時也是重試的入口，ErrorView 的重試按鈕也呼叫這個
    fun loadWeather() {
        // 取消上一個還在執行的請求
        // 例如：使用者選了東京，又馬上選了首爾
        // 這時東京的請求還沒完成，直接取消，只保留首爾的請求
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            // 先切換到 Loading 狀態，讓畫面顯示轉圈
            // 避免使用者看到舊的資料還以為是新的
            _uiState.value = WeatherUiState.Loading

            repository.getWeather(
                lat = _selectedCity.value.latitude,
                lon = _selectedCity.value.longitude
            )
                // 成功：把天氣資料包進 Success 狀態
                .onSuccess { weather ->
                    _uiState.value = WeatherUiState.Success(weather)
                }
                // 失敗：把錯誤訊息包進 Error 狀態
                // Repository 已經把不同錯誤類型轉成人看得懂的訊息
                .onFailure { error ->
                    _uiState.value = WeatherUiState.Error(
                        error.message ?: "無法取得天氣資料"
                    )
                }
        }
    }

    // ── ViewModel 生命週期 ──
    override fun onCleared() {
        super.onCleared()
        // viewModelScope 會在這裡自動取消所有協程
        // 包含 loadJob，不需要手動取消
        // 這裡可以放其他需要清理的資源，例如關閉資料庫連線
    }
}