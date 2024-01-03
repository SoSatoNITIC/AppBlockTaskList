package com.example.appblocktasklist.worker
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UsedApp(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private var mostRecentlyUsedPackage: String? = null

    override suspend fun doWork(): Result {

        //println("call doWork")


        while (true){
            // Get an instance of UsageStatsManager
            val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()


            delay(5000L)
            //Thread.sleep(1000) // wait for 1 seconds

            // Query and aggregate usage stats
            val statsMap = usageStatsManager.queryAndAggregateUsageStats(time - 1000 * 60 * 60 * 24, time)
            mostRecentlyUsedPackage = statsMap.values.maxByOrNull { it.lastTimeUsed }?.packageName

            val localMostRecentlyUsedPackage = mostRecentlyUsedPackage

            if (localMostRecentlyUsedPackage != null) {
                Log.i("Most Recently Used App", localMostRecentlyUsedPackage)
            }
        }


        return Result.success()
    }

}
