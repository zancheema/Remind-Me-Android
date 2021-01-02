package com.sleekdeveloper.remindme.importanttasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleekdeveloper.remindme.data.source.StubRepository
import com.sleekdeveloper.remindme.databinding.FragmentImportantTasksBinding

class ImportantTasksFragment : Fragment() {
    private val viewModel = ImportantTasksViewModel(StubRepository)
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