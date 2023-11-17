package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*

@Entity
data class RockSetting(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val timeRange: String,
    val usableTime: Int,
    val dayOfWeek: String,
    val targetApp: String,
    val unusableTime: Int,
    val preNoticeTiming: String,
    val activeDate: String,
)