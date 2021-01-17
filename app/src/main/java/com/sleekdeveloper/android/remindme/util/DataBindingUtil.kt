package com.sleekdeveloper.android.remindme.util

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sleekdeveloper.android.remindme.R
import com.sleekdeveloper.android.remindme.data.source.domain.Task

@BindingAdapter("app:task_list_items")
fun RecyclerView.setTaskListItems(items: List<Task>) {
    if (adapter == null) adapter = TasksListAdapter()
    (adapter as TasksListAdapter).submitList(items)
}

@BindingAdapter("app:spinner_items")
fun Spinner.setItems(items: List<String>) {
    if (adapter == null) {
        adapter = ArrayAdapter(context, R.layout.task_repeat_spinner, items)
    }
}