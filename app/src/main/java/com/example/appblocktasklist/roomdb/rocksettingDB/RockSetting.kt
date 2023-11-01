package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*

@Entity
data class RockSetting(
    @PrimaryKey val id: Int,
    val taskName: String,
    val memo: String,
    val deadline: String?,
    val nullableLockSettingId: Int?,
    val reason: String,
)