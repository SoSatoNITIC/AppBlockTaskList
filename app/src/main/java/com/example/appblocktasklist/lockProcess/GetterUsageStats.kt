package com.example.appblocktasklist.lockProcess

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.PowerManager
import com.example.appblocktasklist.MyApplication
import java.time.Duration
import java.util.Calendar

class GetterUsageStats(
    private val context: Context
) {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    fun getUsageStatsKeyPackageName(packageName: String, durationMinute: Int): UsageStats? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, -durationMinute)

        return usageStatsManager.queryAndAggregateUsageStats(
            cal.timeInMillis,
            System.currentTimeMillis()
        )[packageName]
    }

    fun getLastUsedApp(): String? {
        val time = System.currentTimeMillis()

        return if (powerManager.isInteractive) {
            // 最後に使用されたアプリのパッケージ名を見つける
            val statsMap = usageStatsManager.queryAndAggregateUsageStats(
                time - Duration.ofDays(60).toMillis(),
                time
            )
            statsMap.values.maxByOrNull { it.lastTimeUsed }?.packageName

        } else {
            null
        }
    }
}