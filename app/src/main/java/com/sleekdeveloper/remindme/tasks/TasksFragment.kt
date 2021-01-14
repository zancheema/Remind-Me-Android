package com.sleekdeveloper.remindme.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sleekdeveloper.remindme.EventObserver
import com.sleekdeveloper.remindme.databinding.FragmentTasksBinding
import com.sleekdeveloper.remindme.tasks.TasksFragmentDirections.Companion.actionTasksFragmentToCreateTaskFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private val viewModel by viewModels<TasksViewModel>()
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
        setUpNavigation()
    }

    private fun setUpNavigation() {
        viewModel.addTaskEvent.observe(viewLifecycleOwner, EventObserver { add ->
            if (add) findNavController().navigate(actionTasksFragmentToCreateTaskFragment())
        })
    }
}