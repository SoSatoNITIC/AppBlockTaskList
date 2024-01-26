package com.example.appblocktasklist.lockProcess

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appblocktasklist.MyApplication
import com.example.appblocktasklist.roomdb.locksettingDB.LockSetting
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit


fun calcRemaining(packageName: String):Duration? {
//    TODO:データベースからデータをとってくる処理にあとで書き換える
//    val lockSettings = MyApplication.database.lockSettingDao().getByPackageName(packageName)

//    検証用サンプル
    val lockSettings = listOf<LockSetting>(LockSetting(
        beginTime = LocalTime.of(9, 10),
        endTime = LocalTime.of(23, 0),
//        usableTime = Duration.ofMinutes(20),
        usableTime = null,
        dayOfWeek = mapOf<DayOfWeek, Boolean>(
            DayOfWeek.MONDAY to true,
            DayOfWeek.TUESDAY to true,
            DayOfWeek.WEDNESDAY to true,
            DayOfWeek.THURSDAY to true,
            DayOfWeek.FRIDAY to true,
            DayOfWeek.SATURDAY to true,
            DayOfWeek.SUNDAY to true
        ),
        targetApp = listOf("com.google.android.youtube"),
        unUsableTime = Duration.ofMinutes(60),
        preNoticeTiming = listOf(Duration.ofMinutes(10)),
        activeDate = null,
    ))
    val remainingTimes = mutableListOf<Duration>()
    for (lockSetting in lockSettings) {
        var remainingTime: Duration? = null

        if(lockSetting.beginTime != null  && lockSetting.endTime != null) {
            remainingTime = calcReminingByTimeRange(lockSetting.beginTime, lockSetting.endTime)
        } else if (lockSetting.usableTime != null){
            val usedTime = MyApplication.usageGetter.
            getUsageStatsKeyPackageName(packageName, 60)?.totalTimeInForeground
            if (usedTime != null) {
                remainingTime = calcReminingByUsableTime(lockSetting.usableTime, usedTime)
            }
        }
        if (remainingTime != null) {
            remainingTimes.add(remainingTime)
        }
    }
    return remainingTimes.minOrNull()
}

fun setLockWorker(context: Context, duration: Duration) {
    val seconds = duration.seconds
    val request = OneTimeWorkRequestBuilder<LockWorker>()
        .addTag("lockWorker")
        .setInitialDelay(seconds, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(request)
}

fun cancelWorker(context: Context) {
    WorkManager.getInstance(context).cancelAllWorkByTag("lockWorker")
}

private fun calcReminingByUsableTime(usable: Duration, usedTime: Long): Duration {
    // 使用済みの時間をDuration型に変換
    val usedTimeDuration = Duration.ofMillis(usedTime)

    // 利用可能な時間から使用済みの時間を引く
    val remainingTime = usable.minus(usedTimeDuration)
    if (remainingTime.isNegative) {
        return Duration.ZERO
    } else {
        return remainingTime
    }
}

private fun calcReminingByTimeRange(beginTime: LocalTime, endTime: LocalTime): Duration {
    if (inTimeRange(beginTime, endTime)) {
        return Duration.ZERO
    } else {
        return calcDurationNow(beginTime)
    }
}

private fun calcDurationNow(targetTime: LocalTime): Duration {
//        24:00を超えた場合を考慮した時間差分計算
    val now = LocalTime.now()

    var duration = Duration.between(now, targetTime)

    if (duration.isNegative) {
        duration = duration.plusHours(24)
    }

    return duration
}

private fun inTimeRange(beginTime: LocalTime, endTime: LocalTime): Boolean {
    val currentTime = LocalTime.now()
    if (beginTime.isBefore(endTime)){
//            通常の場合
        if ((currentTime.isAfter(beginTime) || currentTime.equals(beginTime)) &&
            (currentTime.isBefore(endTime) || currentTime.equals(endTime))) {
            return true
        }
    } else {
//            24:00を挟む場合
        if ((currentTime.isBefore(beginTime) || currentTime.equals(beginTime)) ||
            (currentTime.isAfter(endTime) || currentTime.equals(endTime))) {
            return true
        }
    }
    return false
}

