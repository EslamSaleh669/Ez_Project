package com.example.ezproject.ui.fragment.unit.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.example.ezproject.R
import com.example.ezproject.data.models.UnitDetails
import com.example.ezproject.util.extensions.makeToast
import kotlinx.android.synthetic.main.reservation_date_picker.*
import kotlinx.android.synthetic.main.reservation_date_picker.calendarView
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReservationDatePickerFragment(
    private val unit: UnitDetails? = null,
    private val onDatePickedListener: OnDatePickedListener
) :
    DialogFragment() {

    private var selectedStartDate: Calendar? = null
    private var selectedEndDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reservation_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unitmindays.text= unit?.minDays.toString()
        unitmaxdays.text= unit?.maxDays.toString()
        unit?.let {
            initCalendar()
        }

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                Timber.d("Clicked Date ${dateFormat.format(eventDay.calendar.timeInMillis)}")
                if (!eventDay.isEnabled) {
                    if (selectedStartDate == null || (selectedStartDate != null && selectedEndDate != null)) {
                        selectedStartDate = eventDay.calendar
                        pickuptext.setText(getString(R.string.please_select_check_out_date))

                        selectedEndDate = null
                        startDate.text =
                            SimpleDateFormat(
                                "MMM d",
                                Locale.UK
                            ).format(selectedStartDate?.timeInMillis)
                        startDay.text =
                            SimpleDateFormat("E", Locale.UK).format(selectedStartDate?.timeInMillis)

                        endDate.text =
                            SimpleDateFormat(
                                "MMM d",
                                Locale.UK
                            ).format(selectedStartDate?.timeInMillis)
                        endDay.text =
                            SimpleDateFormat("E", Locale.UK).format(selectedStartDate?.timeInMillis)
                    } else {
                        if (eventDay.calendar.after(selectedStartDate)) {
                             selectedEndDate = eventDay.calendar
                            pickuptext.setText(getString(R.string.both_dates_are_check_out))

                            endDate.text =
                                SimpleDateFormat(
                                    "MMM d",
                                    Locale.UK
                                ).format(selectedEndDate?.timeInMillis)
                            endDay.text = SimpleDateFormat(
                                "E",
                                Locale.UK
                            ).format(selectedEndDate?.timeInMillis)
                        } else {
                            selectedEndDate = selectedStartDate
                            selectedStartDate = eventDay.calendar
                            startDate.text =
                                SimpleDateFormat(
                                    "MMM d",
                                    Locale.UK
                                ).format(selectedStartDate?.timeInMillis)
                            startDay.text = SimpleDateFormat(
                                "E",
                                Locale.UK
                            ).format(selectedStartDate?.timeInMillis)

                            endDate.text =
                                SimpleDateFormat(
                                    "MMM d",
                                    Locale.UK
                                ).format(selectedEndDate?.timeInMillis)
                            endDay.text = SimpleDateFormat(
                                "E",
                                Locale.UK
                            ).format(selectedEndDate?.timeInMillis)
                        }
                    }
                } else {
                    Timber.d("date disabled")
                }

            }
        })




        applyBtn.setOnClickListener {
            unit?.let {

                if (calendarView.selectedDates.size > it.maxDays) {
                    context?.makeToast("You must pick less than ${it.maxDays} day")
                    return@setOnClickListener
                }

                if (calendarView.selectedDates.size < it.minDays) {
                    context?.makeToast("You must pick at least ${it.minDays} day")
                    return@setOnClickListener
                }

                if (selectedStartDate != null && selectedEndDate != null) {
//                val dateStart: String =
//                    SimpleDateFormat("yyyy-M-dd").format(calendar.startDate.timeInMillis)
//                val dateSec: String =
//                    SimpleDateFormat("yyy-M-dd").format(calendar.endDate.timeInMillis)

                    dismiss()
                    onDatePickedListener.onDatePicked(
                        selectedStartDate!!.timeInMillis,
                        selectedEndDate!!.timeInMillis
                    )
                } else {
                    if (it.minDays == 1 && selectedStartDate != null) {
                        dismiss()
                        onDatePickedListener.onDatePicked(
                            selectedStartDate!!.timeInMillis,
                            selectedStartDate!!.timeInMillis
                        )
                        return@setOnClickListener
                    } else {
                        context?.makeToast(getString(R.string.pick_two_dates))

                    }
                }


            }

            dismiss()

        }

    }


    private fun initCalendar() {

        calendarView.setMinimumDate(Calendar.getInstance())

        val disabledDates: ArrayList<Calendar> = ArrayList()
        val lastCalendar = Calendar.getInstance()
        lastCalendar.timeInMillis = dateFormat.parse(unit!!.daysList.last().date).time
        var currentDate = Calendar.getInstance().timeInMillis
        while (currentDate <= lastCalendar.timeInMillis) {
            Timber.d(dateFormat.format(currentDate))
            var currentCalendar = Calendar.getInstance()
            currentCalendar.timeInMillis = currentDate
            if (!isDateAvailable(currentCalendar.timeInMillis)) {
                disabledDates.add(Calendar.getInstance().apply {
                    timeInMillis = currentCalendar.timeInMillis
                })
            }
            currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
            currentDate = currentCalendar.timeInMillis

        }

        calendarView.setDisabledDays(disabledDates)
        calendarView.setMaximumDate(lastCalendar)

    }

    private fun isDateAvailable(dateInMil: Long): Boolean {
        for (day in unit!!.daysList) {
            if (day.date == dateFormat.format(dateInMil)) {
                return day.status == 1
            }
        }
        return false
    }


    interface OnDatePickedListener {
        fun onDatePicked(startDateL: Long, endDateL: Long)

    }


    /*
    *  if (isDateAvailable(eventDay.calendar.timeInMillis)) {

                }
    *
    * */
}