package com.example.appblocktasklist

import android.app.Application
import androidx.room.Room
import com.example.appblocktasklist.lockProcess.GetterUsageStats
import com.example.appblocktasklist.roomdb.AppDB
import kotlin.reflect.KProperty

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDB
        lateinit var usageGetter: GetterUsageStats
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDB::class.java, "AppDB"
        ).build()
        usageGetter = GetterUsageStats(applicationContext)
    }
}