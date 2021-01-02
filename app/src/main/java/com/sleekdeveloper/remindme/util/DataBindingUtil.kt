package com.sleekdeveloper.remindme.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sleekdeveloper.remindme.data.source.domain.Task

@BindingAdapter("app:task_list_items")
fun RecyclerView.setTaskListItems(items: List<Task>) {
    if (adapter == null) adapter = TasksListAdapter()
    (adapter as TasksListAdapter).submitList(items)
}