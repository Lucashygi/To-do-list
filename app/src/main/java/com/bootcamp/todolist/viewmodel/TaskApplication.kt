package com.bootcamp.todolist.viewmodel

import android.app.Application
import com.bootcamp.todolist.database.TaskDatabase
import com.bootcamp.todolist.database.TaskRepository

class TaskApplication : Application() {

    val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}