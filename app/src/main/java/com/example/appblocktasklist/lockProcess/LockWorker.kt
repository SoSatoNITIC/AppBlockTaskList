package com.example.appblocktasklist.lockProcess

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appblocktasklist.Shield

class LockWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val intent = Intent(applicationContext, Shield::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        applicationContext.startActivity(intent)
        return Result.success()
    }
}