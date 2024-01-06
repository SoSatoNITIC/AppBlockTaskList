package com.example.appblocktasklist

import android.app.Application
import androidx.room.Room
import com.example.appblocktasklist.roomdb.AppDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDB
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {

            database = Room.databaseBuilder(
                applicationContext,
                AppDB::class.java, "AppDB"
            ).build()
        }

    }
}