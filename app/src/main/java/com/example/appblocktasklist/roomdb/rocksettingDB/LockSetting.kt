package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class LockSetting(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
//    val timeRange: String,
    val beginTime: LocalTime?,
    val endTime: LocalTime?,
    val usableTime: Duration?,
    val dayOfWeek: Map<DayOfWeek, Boolean>,
    val targetApp: List<String>,
    val unUsableTime: Duration,
    val preNoticeTiming: List<Duration>,
    val activeDate: LocalDate?,
) {init {
    require(((beginTime == null && endTime == null) && usableTime != null) ||
            ((beginTime != null && endTime != null) && usableTime == null)) {
        "Exactly one of the columns must be non-null"
    }
}
}