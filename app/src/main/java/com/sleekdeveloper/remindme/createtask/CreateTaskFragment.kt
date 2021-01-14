package com.sleekdeveloper.remindme.createtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sleekdeveloper.remindme.EventObserver
import com.sleekdeveloper.remindme.databinding.FragmentCreateTaskBinding
import com.sleekdeveloper.remindme.util.setUpSnackar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private val viewModel by viewModels<CreateTaskViewModel>()

    private lateinit var viewDataBinding: FragmentCreateTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentCreateTaskBinding
            .inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        setUpDatePicker()
        setUpTimePicker()
        setUpSnackbar()
        setUpNavigation()
    }

    private fun setUpNavigation() {
        viewModel.taskAddedEvent.observe(viewLifecycleOwner, EventObserver { added ->
            if (added) findNavController().popBackStack()
        })
    }

    private fun setUpSnackbar() {
        view?.setUpSnackar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setUpTimePicker() {
        viewModel.pickTimeEvent.observe(viewLifecycleOwner) {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _: TimePicker, hour: Int, minute: Int ->
                viewModel.setTime(LocalTime.of(hour, minute))
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(requireContext())
        ).show()
    }

    private fun setUpDatePicker() {
        viewModel.pickDateEvent.observe(viewLifecycleOwner) {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                viewModel.setDate(LocalDate.of(year, month + 1, day))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}