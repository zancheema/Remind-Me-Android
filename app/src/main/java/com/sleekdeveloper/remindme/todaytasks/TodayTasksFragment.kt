package com.sleekdeveloper.remindme.todaytasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleekdeveloper.remindme.data.source.StubRepository
import com.sleekdeveloper.remindme.databinding.FragmentTodayTasksBinding

class TodayTasksFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentTodayTasksBinding
    private val viewModel = TodayTasksViewModel(StubRepository)

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