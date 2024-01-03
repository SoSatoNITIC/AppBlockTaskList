package com.example.appblocktasklist.worker
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.os.PowerManager

class UsedApp(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private var mostRecentlyUsedPackage: String? = null

    override suspend fun doWork(): Result {

        //println("call doWork")

        while (true){
            // Get an instance of UsageStatsManager
            val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()

            // Get an instance of PowerManager
            //画面がスリープされているときにカウントしないようにする
            val powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager

            delay(1000L)
            //Thread.sleep(1000) // wait for 1 seconds

            // Query and aggregate usage stats
            val statsMap = usageStatsManager.queryAndAggregateUsageStats(time - 1000 * 60 * 60 * 24, time)
            mostRecentlyUsedPackage = if (powerManager.isInteractive) {
                statsMap.values.maxByOrNull { it.lastTimeUsed }?.packageName
            } else {
                null
            }

            val localMostRecentlyUsedPackage = mostRecentlyUsedPackage

            if (localMostRecentlyUsedPackage != null) {
                Log.i("Most Recently Used App", localMostRecentlyUsedPackage)
            }
        }

        return Result.success()
    }

}
