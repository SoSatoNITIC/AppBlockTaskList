package com.example.appblocktasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appblocktasklist.roomdb.locksettingDB.LockSetting
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class LockViewModel: ViewModel() {
    private var _beginTime= MutableLiveData<LocalTime?>(null)
    val beginTime: LiveData<LocalTime?> = _beginTime

    private var _endTime= MutableLiveData<LocalTime?>(null)
    val endTime: LiveData<LocalTime?> = _endTime

    private var _usableTime= MutableLiveData<Duration?>(null)
    val usableTime: LiveData<Duration?> = _usableTime

    private var _dayOfWeek= MutableLiveData<Map<DayOfWeek, Boolean>>(mapOf<DayOfWeek, Boolean>(
        DayOfWeek.MONDAY to false,
        DayOfWeek.TUESDAY to false,
        DayOfWeek.WEDNESDAY to false,
        DayOfWeek.THURSDAY to false,
        DayOfWeek.FRIDAY to false,
        DayOfWeek.SATURDAY to false,
        DayOfWeek.SUNDAY to false
    ))
    val dayOfWeek: LiveData<Map<DayOfWeek, Boolean>> = _dayOfWeek

    private var _targetApp= MutableLiveData<List<String>>(listOf(""))
    val targetApp: LiveData<List<String>> = _targetApp

    private var _unUsableTime= MutableLiveData<Duration>(Duration.ofMinutes(60))
    val unUsableTime: LiveData<Duration> = _unUsableTime

    //private var _preNoticeTiming= MutableLiveData<List<Duration>>(listOf(Duration.ofMinutes(15), Duration.ofMinutes(30)))
    //val preNoticeTiming: LiveData<List<Duration>> = _preNoticeTiming

    private var _preNoticeTiming= MutableLiveData<List<Duration>>()
    val preNoticeTiming: LiveData<List<Duration>> = _preNoticeTiming

    private var _activeDate= MutableLiveData<LocalDate?>(null)
    val activeDate: LiveData<LocalDate?> = _activeDate

    private var _lockid: MutableLiveData<Int?> = MutableLiveData(0)
    val lockid: LiveData<Int?> = _lockid

    fun setLockId(id: Int?){
        _lockid.value = id
    }



    fun setBeginTime(time: LocalTime?) {
        _beginTime.value = time
    }

    fun setEndTime(time: LocalTime?) {
        _endTime.value = time
    }

    fun setUsableTime(duration: Duration?) {
        _usableTime.value = duration
    }

    fun setDayOfWeek(days: Map<DayOfWeek, Boolean>) {
        _dayOfWeek.value = days
    }

    fun setTargetApp(apps: List<String>) {
        _targetApp.value = apps
    }

    fun setUnUsableTime(duration: Duration) {
        _unUsableTime.value = duration
    }

    fun setPreNoticeTiming(durations: List<Duration>) {
        _preNoticeTiming.value = durations
    }

    fun setActiveDate(date: LocalDate?) {
        _activeDate.value = date
    }

    fun reset() {
        //setLockId(-1)
        setLockId(null)
        setBeginTime(null)
        setEndTime(null)
        setUsableTime(null)
        setDayOfWeek(mapOf<DayOfWeek, Boolean>(
            DayOfWeek.MONDAY to false,
            DayOfWeek.TUESDAY to false,
            DayOfWeek.WEDNESDAY to false,
            DayOfWeek.THURSDAY to false,
            DayOfWeek.FRIDAY to false,
            DayOfWeek.SATURDAY to false,
            DayOfWeek.SUNDAY to false
        ))
        setTargetApp(listOf())
        setUnUsableTime(Duration.ofMinutes(60))
        //setPreNoticeTiming(listOf(Duration.ofMinutes(15), Duration.ofMinutes(30)))
        setPreNoticeTiming(listOf())
        setActiveDate(null)
    }


    fun saveSettings() {
        println("insertData!!!!!!!!")
        val setting = LockSetting(
            beginTime = beginTime.value,
            endTime = endTime.value,
            usableTime = usableTime.value,
            dayOfWeek = dayOfWeek.value ?: mapOf(),
            targetApp = targetApp.value ?: listOf(),
            unUsableTime = Duration.ofMinutes(60),
            preNoticeTiming = preNoticeTiming.value ?: listOf(),
            activeDate = activeDate.value,
            id = if (_lockid.value != null) _lockid.value else null // idフィールドを設定
        )
        // lockSettingDaoインスタンスを取得します
        val dao = MyApplication.database.lockSettingDao() // ここでdaoインスタンスを取得します
        if (_lockid.value != null) {
            println("update!!!!!")
            dao.update(setting)
        } else {
            println("insert!!!!!")
            dao.insertAll(setting)
        }
        println("Complete!!!!!!!!!!!!!")
    }

}