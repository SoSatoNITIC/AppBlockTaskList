package com.example.appblocktasklist.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appblocktasklist.MainActivity
import java.util.concurrent.TimeUnit

//class StartWorker : BroadcastReceiver(){
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            val workRequest = OneTimeWorkRequestBuilder<UsedApp>()
//                .setInitialDelay(0, TimeUnit.SECONDS)
//                .build()
//            WorkManager.getInstance(context).enqueue(workRequest)
//        }
//    }
//}

//class StartWorker : BroadcastReceiver(){
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            // Create an intent for MainActivity
//            val startIntent = Intent(context, MainActivity::class.java)
//            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(startIntent)
//        }
//    }
//}

class StartWorker : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val workRequest = PeriodicWorkRequestBuilder<UsedApp>(1, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

