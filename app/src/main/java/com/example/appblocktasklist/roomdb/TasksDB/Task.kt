package com.example.appblocktasklist.roomdb.TasksDB

import androidx.room.*

@Entity
data class Task(
    @PrimaryKey val id: Int,
    val taskName: String,
    val memo: String,
    val deadline: String?,
    val nullableLockSettingId: Int?,
    val reason: String,
)