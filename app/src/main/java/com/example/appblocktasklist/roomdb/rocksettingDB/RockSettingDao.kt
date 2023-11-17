package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*
import com.example.appblocktasklist.roomdb.TasksDB.Task

@Dao
interface RockSettingDao {
    @Query("SELECT * FROM RockSetting")
    fun getAll(): List<RockSetting>

    @Query("SELECT * FROM RockSetting WHERE id IN (:rockSettingIds)")
    fun getByIds(rockSettingIds: IntArray): List<RockSetting>

    @Insert
    fun insertAll(vararg users: RockSetting)

    @Delete
    fun delete(user: RockSetting)

    @Update
    fun update(task: Task)
}