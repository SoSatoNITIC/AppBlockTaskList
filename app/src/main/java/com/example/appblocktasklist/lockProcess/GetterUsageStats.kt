package com.example.appblocktasklist.lockProcess

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.Calendar

class GetterUsageStats(
    private val context: Context
) {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun getUsageStatsKeyPackageName(packageName: String, durationMinute: Int): UsageStats? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, -durationMinute)

        return usageStatsManager.queryAndAggregateUsageStats(
            cal.timeInMillis,
            System.currentTimeMillis()
        )[packageName]
    }
}