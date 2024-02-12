package com.example.appblocktasklist.roomdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    //LocalDate(タイムゾーンなし日付情報)を文字列に変換
    //@TypeConverter
    //fun fromStringToLocalDate(value: String?): LocalDate? {
    //    return LocalDate.parse(value)
    //}
    //@TypeConverter
    //fun LocalDateToString(date: LocalDate?): String? {
    //    return "%04d-%02d-%02d".format(date?.year, date?.monthValue, date?.dayOfMonth)
    //}
    @TypeConverter
    fun fromStringToLocalDate(value: String?): LocalDate? {
        return if (value == null || value == "null") null else LocalDate.parse(value)
    }
    @TypeConverter
    fun LocalDateToString(date: LocalDate?): String? {
        return if (date == null) null else "%04d-%02d-%02d".format(date?.year, date?.monthValue, date?.dayOfMonth)
    }


    // LocalTime(タイムゾーンなし時間情報)を文字列に変換
    //@TypeConverter
    //fun fromStringToLocalTime(value: String?): LocalTime? {
    //    return LocalTime.parse(value)
    //}
    //@TypeConverter
    //fun LocalTimeToString(date: LocalTime?): String? {
    //    return "%02d:%02d:%02d".format(date?.hour, date?.minute, date?.second)
    //}
    @TypeConverter
    fun fromStringToLocalTime(value: String?): LocalTime? {
        return if (value == null || value == "null") null else LocalTime.parse(value)
    }
    @TypeConverter
    fun LocalTimeToString(date: LocalTime?): String? {
        return if (date == null) null else "%02d:%02d:%02d".format(date.hour, date.minute, date.second)
    }


    //DayOfWeekのリストを変換する
    @TypeConverter
    fun fromDayOfWeekList(value: String?): List<DayOfWeek>? {
        return value?.split(",")?.map { DayOfWeek.valueOf(it) }
    }
    @TypeConverter
    fun DayOfWeekListToString(dayOfWeekList: List<DayOfWeek>?): String? {
        return dayOfWeekList?.joinToString(",") { it.toString() }
    }

    //Duration(時間間隔)を変換する
    @TypeConverter
    fun fromTimestamp(value: Long?): Duration? {
        return if (value == null) null else Duration.ofSeconds(value)
    }
    @TypeConverter
    fun durationToTimestamp(duration: Duration?): Long? {
        return duration?.seconds
    }

    //Duration(時間間隔)のリストを変換する
    @TypeConverter
    fun fromString(value: String?): List<Duration>? {
        return value?.let {
            it.split(",").map { str -> Duration.parse(str) }
        }
    }
    @TypeConverter
    fun durationsToString(durations: List<Duration>?): String? {
        return durations?.joinToString(",") { it.toString() }
    }

    //Stringのリストを変換する
    @TypeConverter
    fun fromStringToStringList(value: String?): List<String>? {
        return value?.split(",")
    }
    @TypeConverter
    fun stringListToString(strings: List<String>?): String? {
        return strings?.joinToString(",")
    }

    //Map<DayOfWeek, Boolean>を変換する
    @TypeConverter
    fun fromStringToMap(value: String): Map<DayOfWeek, Boolean>? {
        val gson = Gson()
        val type = object : TypeToken<Map<DayOfWeek, Boolean>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMapToString(map: Map<DayOfWeek, Boolean>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}