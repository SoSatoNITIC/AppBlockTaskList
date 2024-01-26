package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*
import com.example.appblocktasklist.roomdb.TasksDB.Task

@Dao
interface lockSettingDao {
    @Query("SELECT * FROM LockSetting")
    fun getAll(): List<LockSetting>

    @Query("SELECT * FROM LockSetting WHERE id IN (:rockSettingIds)")
    fun getByIds(rockSettingIds: IntArray): List<LockSetting>

    @Insert
    fun insertAll(vararg users: LockSetting)

    @Delete
    fun delete(user: LockSetting)

    //@Update
    //fun update(task: Task)
    @Update
    fun update(lockSetting: LockSetting)
}