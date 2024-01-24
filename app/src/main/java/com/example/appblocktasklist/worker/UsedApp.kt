package com.example.appblocktasklist.worker
import android.app.usage.UsageStatsManager
import android.content.Context
import android.app.ActivityManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.delay
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.os.PowerManager
import java.text.SimpleDateFormat
import java.util.*

class UsedApp(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private var mostRecentlyUsedPackage: String? = null

    // モニタリングするアプリのリスト
    var appName = mutableListOf<String>("YouTube","com.google.android.apps.youtube.music","tv.abema")

    override suspend fun doWork(): Result {

        // アプリの使用状況を継続的にチェック
        while (true){
            // UsageStatsManagerサービスを取得
            val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()

            // PowerManagerサービスを取得してスリープ中にはカウントしないようにする
            val powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager

            // 1秒待つ
            delay(1000L)

            // 過去2ヶ月の使用統計を取得し集計
            val statsMap = usageStatsManager.queryAndAggregateUsageStats(time - 1000L * 60* 60 * 24 * 60, time)
            mostRecentlyUsedPackage = if (powerManager.isInteractive) {
                // 最後に使用されたアプリのパッケージ名を見つける
                statsMap.values.maxByOrNull { it.lastTimeUsed }?.packageName
            } else {
                null
            }

            val localMostRecentlyUsedPackage = mostRecentlyUsedPackage

            // 最後に使用されたパッケージが見つかった場合
            if (localMostRecentlyUsedPackage != null) {
//                Log.i("Most Recently Used App", localMostRecentlyUsedPackage)

                // PackageManagerサービスを取得
                val packageManager = applicationContext.packageManager

                // アプリ情報を取得
                var appInfo: ApplicationInfo? = null
                try {
                    appInfo = packageManager.getApplicationInfo(localMostRecentlyUsedPackage, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e("Package Manager Error", "Failed to get application info for $localMostRecentlyUsedPackage")
                }

                // アプリのラベルを取得
                val appLabel = appInfo?.let { packageManager.getApplicationLabel(it).toString() }

                // これでlocalMostRecentlyUsedPackageの代わりにappLabelを使用できます
                Log.i("Most Recently Used App", appLabel ?: localMostRecentlyUsedPackage)

                // 最後に使用されたパッケージがモニタリングするアプリのリストにあるかチェック
                for (i in appName){
                    if(appLabel == i){
                        hirakuFunction()
                    }
                }
            }else{
                println("最近のアプリ使用状況が見つかりませんでした。")
            }

            // ホーム画面が表示されているか確認
            //val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            //val runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE)
            //val homePackageName = runningTasks.firstOrNull()?.baseIntent?.component?.packageName

            //if (homePackageName != null && homePackageName == "com.android.launcher") {
            //    Log.i("Home Screen Status", "Home Screen is visible")
            //} else {
            //    Log.i("Home Screen Status", "Home Screen is not visible")
            //}


        }

        return Result.success()
    }

    // モニタリングするアプリがリストにある場合に実行する関数
    fun hirakuFunction(){
        return println("対象アプリが見つかりました！")
    }
}
