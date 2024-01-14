package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime

@Entity
data class RockSetting(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
//    val timeRange: String,
    val beginTime: LocalTime?,
    val endTime: LocalTime?,
    val usableTime: Duration?,
    val dayOfWeek: List<DayOfWeek>,
    val targetApp: String,
    val unUsableTime: Duration,
    val preNoticeTiming: List<Duration>,
    val activeDate: String?,
) {init {
    require(((beginTime == null && endTime == null) && usableTime != null) ||
            ((beginTime != null && endTime != null) && usableTime == null)) {
        "Exactly one of the columns must be non-null"
    }
}
}