package com.bootcamp.todolist.database

import androidx.room.*
import com.bootcamp.todolist.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY id")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Task

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task:Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task:Task)
}