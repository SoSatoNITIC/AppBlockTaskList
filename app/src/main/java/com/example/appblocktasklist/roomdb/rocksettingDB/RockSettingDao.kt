package com.example.appblocktasklist.roomdb.rocksettingDB

import androidx.room.*
@Dao
interface RockSettingDao {
    @Query("SELECT * FROM RockSetting")
    fun getAll(): List<RockSetting>

    @Query("SELECT * FROM RockSetting WHERE id IN (:rockSettingIds)")
    fun loadAllByIds(rockSettingIds: IntArray): List<RockSetting>

    @Insert
    fun insertAll(vararg users: RockSetting)

    @Delete
    fun delete(user: RockSetting)
}