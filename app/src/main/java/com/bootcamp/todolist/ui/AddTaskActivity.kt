package com.bootcamp.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bootcamp.todolist.databinding.ActivityAddTaskBinding
import com.bootcamp.todolist.extensions.format
import com.bootcamp.todolist.extensions.text
import com.bootcamp.todolist.model.Task
import com.bootcamp.todolist.viewmodel.TaskApplication
import com.bootcamp.todolist.viewmodel.TaskViewModel
import com.bootcamp.todolist.viewmodel.TaskViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            taskViewModel.getById(taskId).observe(this, {
                if (it != null) {
                    binding.inputLayoutTitle.text = it.title
                    binding.inputLayoutDescription.text = it.description.toString()
                    binding.tillDate.text = it.date
                    binding.tillHour.text = it.hour
                }
            })
        }
        setContentView(binding.root)
        insertListeners()
    }


    private fun insertListeners() {
        binding.tillDate.editText?.setOnClickListener {
            val datepicker = MaterialDatePicker.Builder.datePicker().build()
            datepicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tillDate.text = Date(it + offset).format()
            }
            datepicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
        binding.tillHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.tillHour.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnNewTask.setOnClickListener {
            val taskToInsert = Task(
                id = intent.getIntExtra(TASK_ID, 0),
                title = binding.inputLayoutTitle.text,
                description = binding.inputLayoutDescription.text,
                date = binding.tillDate.text,
                hour = binding.tillHour.text
            )
            if (intent.hasExtra(TASK_ID)) {
                taskViewModel.update(taskToInsert)
            } else {
                taskViewModel.insert(taskToInsert)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}