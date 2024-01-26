package com.example.appblocktasklist.roomdb.locksettingDB

import androidx.room.*

@Dao
interface LockSettingDao {
    @Query("SELECT * FROM LockSetting")
    fun getAll(): List<LockSetting>

    @Query("SELECT * FROM LockSetting WHERE id IN (:lockSettingIds)")
    fun getByIds(lockSettingIds: IntArray): List<LockSetting>

    @Insert
    fun insertAll(vararg users: LockSetting)

    @Delete
    fun delete(user: LockSetting)

    //@Update
    //fun update(task: Task)
    @Update
    fun update(lockSetting: LockSetting)
}