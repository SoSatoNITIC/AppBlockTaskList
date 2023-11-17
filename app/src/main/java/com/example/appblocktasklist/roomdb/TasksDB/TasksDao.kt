package com.example.appblocktasklist.roomdb.TasksDB

import androidx.room.*
@Dao
interface TasksDao {
    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM Task WHERE id IN (:userIds)")
    fun getByIds(userIds: IntArray): List<Task>

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)
}