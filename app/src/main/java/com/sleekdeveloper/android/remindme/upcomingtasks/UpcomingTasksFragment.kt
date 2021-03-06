package com.sleekdeveloper.android.remindme.upcomingtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sleekdeveloper.android.remindme.databinding.FragmentUpcomingTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingTasksFragment : Fragment() {

    private val viewModel by viewModels<UpcomingTasksViewModel>()

    private lateinit var viewDataBinding: FragmentUpcomingTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentUpcomingTasksBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
    }
}