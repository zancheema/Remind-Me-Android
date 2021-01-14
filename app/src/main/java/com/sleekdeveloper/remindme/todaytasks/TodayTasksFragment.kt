package com.sleekdeveloper.remindme.todaytasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sleekdeveloper.remindme.databinding.FragmentTodayTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayTasksFragment : Fragment() {

    private val viewModel by viewModels<TodayTasksViewModel>()

    private lateinit var viewDataBinding: FragmentTodayTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentTodayTasksBinding.inflate(inflater, container, false)
            .apply {viewmodel = viewModel}
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
    }
}