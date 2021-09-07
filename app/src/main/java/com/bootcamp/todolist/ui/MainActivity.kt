package com.bootcamp.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todolist.R
import com.bootcamp.todolist.database.TaskDatabase
import com.bootcamp.todolist.database.TaskRepository
import com.bootcamp.todolist.databinding.ActivityMainBinding
import com.bootcamp.todolist.datasource.TaskDataSource
import com.bootcamp.todolist.viewmodel.TaskApplication
import com.bootcamp.todolist.viewmodel.TaskViewModel
import com.bootcamp.todolist.viewmodel.TaskViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()
        insertListeners()
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
        adapter.listenerDelete = {
            taskViewModel.delete(it)
            Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        taskViewModel.allTasks.observeForever { taskList ->
            // Update the cached copy of the words in the adapter.
            binding.includeEmptyState.emptyState.visibility = if (taskList.isEmpty()) View.VISIBLE
            else View.GONE
            taskList.let { adapter.submitList(it) }
        }
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}
