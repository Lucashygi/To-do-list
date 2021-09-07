package com.bootcamp.todolist.viewmodel

import androidx.lifecycle.*
import com.bootcamp.todolist.database.TaskRepository
import com.bootcamp.todolist.model.Task


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()

    fun insert(task: Task) = repository.insert(task)

    fun getById(taskId: Int): LiveData<Task> = repository.taskById(taskId).asLiveData()

    fun delete(task: Task) {
        repository.delete(task)
    }

    fun update(task:Task) {
        repository.update(task)
    }

}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
