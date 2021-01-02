package com.sleekdeveloper.remindme.delayedtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleekdeveloper.remindme.data.source.StubRepository
import com.sleekdeveloper.remindme.databinding.FragmentDelayedTasksBinding

class DelayedTasksFragment : Fragment() {
    private val viewModel = DelayedTasksViewModel(StubRepository)
    private lateinit var viewDataBinding: FragmentDelayedTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with data binding
        viewDataBinding = FragmentDelayedTasksBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
    }
}