package com.example.appblocktasklist

import android.app.Application
import androidx.room.Room
import com.example.appblocktasklist.roomdb.AppDB

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDB
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDB::class.java, "AppDB"
        ).build()
    }
}