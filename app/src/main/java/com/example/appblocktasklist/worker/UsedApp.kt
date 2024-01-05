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


    // com.google.android.permissioncontroller をどう扱うか用検討　たまーにパーミッション許可を求めていない時でもこれがずっと表示されるアプリがある
    var appName = mutableListOf<String>("com.google.android.youtube","com.google.android.apps.youtube.music","tv.abema")

    override suspend fun doWork(): Result {

        //println("call doWork")

        while (true){
            // Get an instance of UsageStatsManager
            val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()

            // Get an instance of PowerManager
            //画面がスリープされているときにカウントしないようにする
            val powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager

            delay(1000L) // wait for 1 seconds

            // Query and aggregate usage stats
            val statsMap = usageStatsManager.queryAndAggregateUsageStats(time - 1000L * 60* 60 * 24 * 60, time)//2month
            mostRecentlyUsedPackage = if (powerManager.isInteractive) {
                statsMap.values.maxByOrNull { it.lastTimeUsed }?.packageName
            } else {
                null
            }

            val localMostRecentlyUsedPackage = mostRecentlyUsedPackage

            if (localMostRecentlyUsedPackage != null) {
                Log.i("Most Recently Used App", localMostRecentlyUsedPackage)
            }else{
                println("null!!")
            }

            //対象アプリを見つける
            for (i in appName){
                if(localMostRecentlyUsedPackage == i){
                    hirakuFunction()
                }
            }
        }

        return Result.success()
    }

    fun hirakuFunction(){
        return println("findApp!!")
    }

}
