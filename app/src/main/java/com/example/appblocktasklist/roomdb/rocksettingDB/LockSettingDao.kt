package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*
import com.example.appblocktasklist.roomdb.TasksDB.Task

@Dao
interface LockSettingDao {
    @Query("SELECT * FROM LockSetting")
    fun getAll(): List<LockSetting>

    @Query("SELECT * FROM LockSetting WHERE id IN (:lockSettingIds)")
    fun getByIds(lockSettingIds: IntArray): List<LockSetting>

    @Query("SELECT * FROM LockSetting WHERE targetApp LIKE :packageName")
    fun getByPackageName(packageName: String): List<LockSetting>

    @Insert
    fun insertAll(vararg users: LockSetting)

    @Delete
    fun delete(user: LockSetting)

    //@Update
    //fun update(task: Task)
    @Update
    fun update(lockSetting: LockSetting)
}