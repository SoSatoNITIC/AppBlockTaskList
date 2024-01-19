package com.example.appblocktasklist

import androidx.lifecycle.ViewModel
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class LockViewModel: ViewModel() {
    var beginTime: LocalTime? = null
    var endTime: LocalTime? = null
    var usableTime: Duration? = null
    var dayOfWeek: Map<DayOfWeek, Boolean> = mapOf<DayOfWeek, Boolean>(
        DayOfWeek.MONDAY to false,
        DayOfWeek.TUESDAY to false,
        DayOfWeek.WEDNESDAY to false,
        DayOfWeek.THURSDAY to false,
        DayOfWeek.FRIDAY to false,
        DayOfWeek.SATURDAY to false,
        DayOfWeek.SUNDAY to false
    )
    var targetApp: List<String> = listOf("")
    var unUsableTime: Duration = Duration.ofMinutes(60)
    var preNoticeTiming: List<Duration> = listOf(Duration.ofMinutes(15), Duration.ofMinutes(30))
    var activeDate: LocalDate? = null

    fun reset() {
        beginTime = null
        endTime = null
        usableTime = null
        dayOfWeek = mapOf<DayOfWeek, Boolean>(
            DayOfWeek.MONDAY to false,
            DayOfWeek.TUESDAY to false,
            DayOfWeek.WEDNESDAY to false,
            DayOfWeek.THURSDAY to false,
            DayOfWeek.FRIDAY to false,
            DayOfWeek.SATURDAY to false,
            DayOfWeek.SUNDAY to false
        )
        targetApp = listOf("")
        unUsableTime = Duration.ofMinutes(60)
        preNoticeTiming = listOf(Duration.ofMinutes(15), Duration.ofMinutes(30))
        activeDate = null
    }
}