package com.example.ezproject.ui.fragment.unit.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.ezproject.R
import com.example.ezproject.data.network.response.UnitDraftResponse
import com.example.ezproject.util.extensions.makeToast
import kotlinx.android.synthetic.main.pick_unit_available_dates_dialog.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UnitAvailableDatesCalendarDialog(
    private val unit: UnitDraftResponse.Unit,
    private val onRangePicked: OnDateRangePicked,
    private val currency: String
) :
    DialogFragment() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.UK)
    private var selectedStartDate: Calendar? = null
    private var selectedEndDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pick_unit_available_dates_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView.setDisabledDays(disabledDates())
        calendarView.setMinimumDate(Calendar.getInstance())
        currencyTxt.text = currency

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                Timber.d("Clicked Date ${dateFormat.format(eventDay.calendar.timeInMillis)}")
                if (!eventDay.isEnabled) {
                    if (selectedStartDate == null || (selectedStartDate != null && selectedEndDate != null)) {
                        selectedStartDate = eventDay.calendar
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
        pickBtn.setOnClickListener {
            val price: String = initPrice.text.trim().toString()
            if (selectedStartDate != null && selectedEndDate != null && price.isNotEmpty()) {
                dismiss()
                onRangePicked.onRangePicked(
                    calendarView.selectedDates,
                    selectedStartDate!!,
                    selectedEndDate!!,
                    price
                )

            } else {
                context?.makeToast("You must pick start and end date and enter initial price")
            }
        }

    }

    private fun disabledDates(): List<Calendar> {
        val disabledDates = ArrayList<Calendar>()
        for (date in unit.dates) {

            disabledDates.add(Calendar.getInstance().apply {
                timeInMillis = dateFormat.parse(date.date).time
            })

        }

        return disabledDates
    }

    interface OnDateRangePicked {
        fun onRangePicked(
            pickedDays: List<Calendar>,
            startDateL: Calendar,
            endDateL: Calendar,
            price: String
        )

    }
}