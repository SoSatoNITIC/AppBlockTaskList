package com.example.appblocktasklist.roomdb

import androidx.room.*
import com.example.appblocktasklist.roomdb.TasksDB.Task
import com.example.appblocktasklist.roomdb.TasksDB.TasksDao
import com.example.appblocktasklist.roomdb.rocksettingDB.LockSetting
import com.example.appblocktasklist.roomdb.rocksettingDB.lockSettingDao

@Database(entities = [Task::class, LockSetting::class],version = 1)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun rocksettingDao(): lockSettingDao
}