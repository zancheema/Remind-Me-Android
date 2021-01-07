package com.sleekdeveloper.remindme.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleekdeveloper.remindme.data.source.StubRepository
import com.sleekdeveloper.remindme.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {
    private val viewModel = TasksViewModel(StubRepository)
    private lateinit var viewDataBinding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentTasksBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
    }
}