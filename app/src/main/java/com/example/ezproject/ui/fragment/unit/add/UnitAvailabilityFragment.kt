package com.example.ezproject.ui.fragment.unit.add

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.network.response.UnitDraftResponse
import com.example.ezproject.ui.adapter.UnitDatesAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_availablity.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class UnitAvailabilityFragment : Fragment(), UnitDatesAdapter.OnDateClicked {

    @Inject
    @field:Named("unitDraft")
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private val viewModel: AddUnitViewModel by lazy {
        ViewModelProvider(activity!!, viewModeFactory).get(AddUnitViewModel::class.java)
    }

    private val autoDispose = AutoDispose()
    private val readDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_unit_availablity, container, false)
    }


    /*
    * dates must be more than 0
    *
    * */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.unitDraft?.let {
            initView()
        } ?: autoDispose.add(
            viewModel.loadUnitDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    viewModel.unitDraft = it
                    initView()
                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )


        nextBtn.setOnClickListener {
            val datesNum = viewModel.unitDraft?.unit?.dates?.size ?: 0
            if (datesNum > 1) {
                autoDispose.add(
                    viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            if (it.status == 1) {
                                context?.makeToast("Done")
                            }
                            activity?.supportFragmentManager?.commit {
                                replace(R.id.fragmentContainer, AddUnitPricingFragment())
                                addToBackStack("")
                            }
                        },
                        {
                            Timber.e(it)
                            context?.handleApiError(it)
                        })
                )
            } else {
                context?.makeToast(getString(R.string.dates_required))
            }
        }
    }

    private fun initView() {
        viewModel.unitDraft?.let {
            currencyTxt.text = "Prices in ${viewModel.currency}"
            if (it.unit.dates.isNotEmpty()) {
                val startCalendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = readDateFormat.parse(it.unit.dates.first().date).time
                }
                val endCalendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = readDateFormat.parse(it.unit.dates.last().date).time
                }
                displayRangeBoundaries(startCalendar, endCalendar)
                daysRecycler.adapter = UnitDatesAdapter(it.unit, activity!!, this)
                daysRecycler.layoutManager = GridLayoutManager(context, 3)
                daysRecycler.visibility = View.VISIBLE
            } else {
                displayRangeBoundaries(Calendar.getInstance(), Calendar.getInstance())
            }



            datesLayout.setOnClickListener { pickDates ->
                UnitAvailableDatesCalendarDialog(
                    it.unit,
                    object : UnitAvailableDatesCalendarDialog.OnDateRangePicked {
                        override fun onRangePicked(
                            pickedDays: List<Calendar>,
                            selectedStartDate: Calendar,
                            selectedEndDate: Calendar,
                            price: String
                        ) {

                            for (date in pickedDays) {
                                viewModel.unitDraft?.unit?.dates?.add(
                                    UnitDraftResponse.Date(
                                        date = readDateFormat.format(date.timeInMillis),
                                        id = null,
                                        price = price,
                                        priceBefore = null,
                                        status = "1",
                                        unitId = viewModel.unitDraft?.unit?.id
                                    )
                                )

                                viewModel.unitDraft?.unit?.days?.add(
                                    UnitDraftResponse.Day(
                                     dateStart = readDateFormat.format(selectedStartDate.timeInMillis),
                                     dateEnd = readDateFormat.format(selectedEndDate.timeInMillis),
                                     dayPrice = price,
                                     dayPriceBefore = price,
                                     unitId = viewModel.unitDraft!!.unit.id,
                                     createdAt = "null",
                                      id = viewModel.unitDraft!!.unit.userId ,
                                        updatedAt = ""
                                    )
                                )
                            }

                            viewModel.unitDraft?.unit?.dates?.sortBy {
                                readDateFormat.parse(it.date).time
                            }
                            val startCalendar: Calendar = Calendar.getInstance().apply {
                                timeInMillis = readDateFormat.parse(it.unit.dates.first().date).time
                            }
                            val endCalendar: Calendar = Calendar.getInstance().apply {
                                timeInMillis = readDateFormat.parse(it.unit.dates.last().date).time
                            }
                            displayRangeBoundaries(startCalendar, endCalendar)

                            daysRecycler.adapter =
                                UnitDatesAdapter(it.unit, activity!!, this@UnitAvailabilityFragment)
                            daysRecycler.layoutManager = GridLayoutManager(context, 3)
                            daysRecycler.visibility = View.VISIBLE

                        }
                    }, viewModel.currency
                ).show(activity!!.supportFragmentManager, "")
            }
        }
    }


    fun displayRangeBoundaries(selectedStartDate: Calendar, selectedEndDate: Calendar) {
        startDate.text =
            SimpleDateFormat(
                "MMM d",
                Locale.UK
            ).format(selectedStartDate.timeInMillis)
        startDay.text =
            SimpleDateFormat(
                "E",
                Locale.UK
            ).format(selectedStartDate.timeInMillis)

        endDate.text =
            SimpleDateFormat(
                "MMM d",
                Locale.UK
            ).format(selectedEndDate.timeInMillis)
        endDay.text = SimpleDateFormat(
            "E",
            Locale.UK
        ).format(selectedEndDate.timeInMillis)


    }

    override fun onDateClicked(date: UnitDraftResponse.Date) {
        val index = viewModel.unitDraft?.unit?.dates?.indexOf(date)
        daysRecycler.adapter?.let { adapter ->
            index?.let {
                Dialog(context!!).apply {
                    setContentView(R.layout.dialog_discount_price)
                    findViewById<Button>(R.id.doneBtn).setOnClickListener {
                        val newPriceInput = findViewById<TextView>(R.id.newPrice)
                        if (newPriceInput.text.trim().toString().isNotEmpty()) {
                            date.priceBefore = newPriceInput.text.toString()
                            viewModel.unitDraft?.unit?.dates?.set(index, date)
                            (adapter as UnitDatesAdapter).updateData(viewModel.unitDraft!!.unit)
                        }
                        dismiss()
                    }
                }.show()
            }
        }
    }

}