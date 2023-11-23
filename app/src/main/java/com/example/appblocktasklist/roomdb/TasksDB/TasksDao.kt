package com.example.appblocktasklist.roomdb.TasksDB

import androidx.room.*
@Dao
interface TasksDao {
    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM Task WHERE id IN (:userId)")
    fun getTask(userId: Int): Task
    @Query("SELECT * FROM Task WHERE id IN (:userIds)")
    fun getTasks(userIds: IntArray): List<Task>

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)
}