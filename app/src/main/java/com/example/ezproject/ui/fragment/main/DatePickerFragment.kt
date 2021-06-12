package com.example.ezproject.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.ezproject.R
import com.example.ezproject.util.extensions.makeToast
import kotlinx.android.synthetic.main.date_picker_layout.*
import java.util.*


class DatePickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.date_picker_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView.setMinimumDate(Calendar.getInstance())
        calendarView.setDisabledDays(arrayListOf(Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 20)
        }))


    }
}