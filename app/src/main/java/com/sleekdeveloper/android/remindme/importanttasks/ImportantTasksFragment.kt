package com.sleekdeveloper.android.remindme.importanttasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sleekdeveloper.android.remindme.databinding.FragmentImportantTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportantTasksFragment : Fragment() {

    private val viewModel by viewModels<ImportantTasksViewModel>()

    private lateinit var viewDataBinding: FragmentImportantTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentImportantTasksBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
    }
}