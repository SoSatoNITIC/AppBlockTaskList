package com.example.appblocktasklist.roomdb

import androidx.room.*
import com.example.appblocktasklist.roomdb.TasksDB.Task
import com.example.appblocktasklist.roomdb.TasksDB.TasksDao
import com.example.appblocktasklist.roomdb.rocksettingDB.RockSetting
import com.example.appblocktasklist.roomdb.rocksettingDB.RockSettingDao

@Database(entities = [Task::class, RockSetting::class],version = 1)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun rocksettingDao(): RockSettingDao
}