package com.example.core.data.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(this, formatter)
    val month = date.monthValue
    val day = date.dayOfMonth
    val dayOfWeek = when (date.dayOfWeek) {
        DayOfWeek.MONDAY    -> "週一"
        DayOfWeek.TUESDAY   -> "週二"
        DayOfWeek.WEDNESDAY -> "週三"
        DayOfWeek.THURSDAY  -> "週四"
        DayOfWeek.FRIDAY    -> "週五"
        DayOfWeek.SATURDAY  -> "週六"
        DayOfWeek.SUNDAY    -> "週日"
    }
    return "$month/$day($dayOfWeek)"
}