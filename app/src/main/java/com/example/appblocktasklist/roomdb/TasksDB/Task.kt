package com.example.appblocktasklist.roomdb.TasksDB

import androidx.room.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var taskName: String,
    var memo: String,
    var deadline: String? = null,
    var nullableLockSettingId: Int? = null,
    var reason: String,
)
