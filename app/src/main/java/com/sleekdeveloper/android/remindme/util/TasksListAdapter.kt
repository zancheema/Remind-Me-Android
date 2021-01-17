package com.sleekdeveloper.android.remindme.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.databinding.TaskListItemBinding

class TasksListAdapter : ListAdapter<Task, TasksListAdapter.ViewHolder>(TasksDiffUtil()) {

    class ViewHolder private constructor(
        private val binding: TaskListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task) {
            binding.task = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater =  LayoutInflater.from(parent.context)
                val binding = TaskListItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class TasksDiffUtil : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) =
        oldItem == newItem
}
