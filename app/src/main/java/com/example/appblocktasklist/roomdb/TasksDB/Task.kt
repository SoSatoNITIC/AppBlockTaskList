package com.example.appblocktasklist.roomdb.TasksDB

import androidx.room.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val taskName: String,
    val memo: String,
    val deadline: String? = null,
    val nullableLockSettingId: Int? = null,
    val reason: String,
)
