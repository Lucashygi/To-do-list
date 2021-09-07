package com.bootcamp.todolist.database

import androidx.annotation.WorkerThread
import com.bootcamp.todolist.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {
    val allTasks: Flow<List<Task>> = taskDao.getAll()

    fun insert(task: Task) {
        Thread() {
            taskDao.insert(task)
        }.start()
    }

    fun taskById(id: Int): Task = taskDao.findById(id)

    fun delete(task: Task) {
        Thread(){
            taskDao.delete(task)
        }.start()
    }
}