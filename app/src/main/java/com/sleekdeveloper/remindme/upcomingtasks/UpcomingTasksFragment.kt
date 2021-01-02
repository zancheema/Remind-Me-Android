package com.sleekdeveloper.remindme.upcomingtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleekdeveloper.remindme.data.source.StubRepository
import com.sleekdeveloper.remindme.databinding.FragmentUpcomingTasksBinding

class UpcomingTasksFragment : Fragment() {
    private val viewModel = UpcomingTasksViewModel(StubRepository)
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