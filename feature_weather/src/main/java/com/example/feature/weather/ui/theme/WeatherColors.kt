package com.example.feature.weather.ui.theme

import androidx.compose.ui.graphics.Color

object WeatherColors {
    // 背景
    val Background         = Color(0xFF090E18)

    // 強調色
    val Primary            = Color(0xFF90C0F0)

    // 文字
    val TextPrimary        = Color(0xFFEEF4FF)
    val TextSecondary      = Color(0xFF8AA0C0)
    // CityListScreen 城市名稱
    val TextMuted          = Color(0xFF6080A0)
    val TextNavUnselectedBottomBar  = Color(0xFF2A3A50)  // BottomBar 未選中

    // 卡片背景
    val CardBg             = Color(0xFF0F1825)  // 一般卡片
    val CardBgToday         = Color(0x14508CDC)  // TodayScreen 小卡片（半透明）
    val CardBgSelected     = Color(0xFF0F1E35)  // 選中卡片（城市、週預報今天）

    // 卡片邊框
    val CardBorder         = Color(0x1A508CDC)  // 一般邊框
    val CardBorderToday     = Color(0x33508CDC)  // TodayScreen 小卡片邊框
    val CardBorderSelected = Color(0x66508CDC)  // 選中邊框

    // 分隔線
    val DividerToday       = Color(0x33508CDC)  // TodayScreen 用

    val RegionLabel        = Color(0xFF6080A0)  // 城市地區標題
}