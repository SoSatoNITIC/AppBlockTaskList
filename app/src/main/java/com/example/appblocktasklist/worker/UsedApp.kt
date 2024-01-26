package com.example.appblocktasklist.worker
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appblocktasklist.MyApplication
import com.example.appblocktasklist.lockProcess.calcRemaining
import com.example.appblocktasklist.lockProcess.cancelWorker
import com.example.appblocktasklist.lockProcess.setLockWorker
import kotlinx.coroutines.delay

class UsedApp(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private var mostRecentlyUsedPackage: String? = null
    private var beforeUsedApp: String? = null

    // モニタリングするアプリのリスト
    val targetAppNames = mutableSetOf<String>("com.google.android.youtube","com.google.android.apps.youtube.music","tv.abema")

    override suspend fun doWork(): Result {

        // アプリの使用状況を継続的にチェック
        while (true){
            // PowerManagerサービスを取得してスリープ中にはカウントしないようにする

            // 1秒待つ
            delay(1000L)
            updateTargetAppList()

            // 過去2ヶ月の使用統計を取得し集計
            mostRecentlyUsedPackage = MyApplication.usageGetter.getLastUsedApp()

            // 最後に使用されたパッケージが見つかった場合
            if (mostRecentlyUsedPackage != null) {
                Log.i("Most Recently Used App", mostRecentlyUsedPackage!!)
                if (beforeUsedApp != mostRecentlyUsedPackage) {
//                    ここに呼び出しを書く
                    println("画面が切り替わりました")
                    if (targetAppNames.contains(mostRecentlyUsedPackage)) {
                        val duration = calcRemaining(mostRecentlyUsedPackage!!)

                        if (duration != null) {
                            cancelWorker(applicationContext)
                            setLockWorker(applicationContext, duration)
                        }
                    }
                    beforeUsedApp = mostRecentlyUsedPackage
                }

            }else{
                println("最近のアプリ使用状況が見つかりませんでした。")
            }
        }
        return Result.success()
    }

    private fun updateTargetAppList() {
        val lockList = MyApplication.database.lockSettingDao().getAll()
        val packages = lockList.flatMap { it.targetApp }
        targetAppNames.clear()
        targetAppNames.addAll(packages.toMutableSet())
        print(packages)
    }
}
