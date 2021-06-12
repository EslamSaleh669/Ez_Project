package com.example.ezproject.ui.fragment.unit.filter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.ui.fragment.AllFilteredHotelsFragment
import com.example.ezproject.ui.fragment.AllHotelssFragment
import com.example.ezproject.ui.fragment.unit.reservation.ReservationDatePickerFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.collapse
import com.example.ezproject.util.extensions.expand
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.dialog_filter.*
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class FilterHotelsDialog : Fragment() {


    @Inject
    @field:Named("filter")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FilterViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(FilterViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()
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
        return inflater.inflate(R.layout.dialog_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences( Constants.preferences_fileName,0)
        var sharedIdValue = sharedPreferences.getInt("selid",0)

        applyBtn.setOnClickListener {

            viewModel.setFilterCounts(
                adultsCount.text.toString(),
                childCount.text.toString(),
                bedsCount.text.toString(),
                roomsCount.text.toString(),
                bathCount.text.toString(),
                sharedIdValue
            )

            Log.d("sharedvalues",sharedIdValue.toString())

            (activity as AppCompatActivity).supportFragmentManager.commit {
                replace(
                    R.id.fragmentContainer,
                    AllFilteredHotelsFragment()
                )
                addToBackStack("")
            }
        }

        sb_range_3.setIndicatorTextDecimalFormat("0.0")
        sb_range_3.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                viewModel.addPrices(leftValue, rightValue)

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }
        })
        startPrice.text = "100 ${viewModel.currentCurrency}"
        endPrice.text = "10000 ${viewModel.currentCurrency}"


        sb_range_3.setProgress(100f, 10000f)
        sortBy.setOnClickListener {
            val popup = PopupMenu(context!!, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.sort_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener {
                sortBy.text = it.title
                viewModel.setOrderBy(it.titleCondensed.toString())
                true
            }

        }
        autoDispose.add(
            viewModel.filterItems("aminites").observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    it.response.forEach { item ->
                        val optionView = LayoutInflater.from(context)
                            .inflate(R.layout.filter_option_item_layout, ameOptionsLayout, false)
                        optionView.findViewById<CheckBox>(R.id.optionCheck)
                            .setOnCheckedChangeListener { btn, state ->
                                viewModel.add2Amenities(item.id)

                            }
                        optionView.setOnClickListener {
                            optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                        }

                        val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(Constants.LANG_KEY,"en")

                        if (lan.equals("ar")){
                            optionView.findViewById<TextView>(R.id.optionTitle).text = item.name
                        }else{
                            optionView.findViewById<TextView>(R.id.optionTitle).text = item.nameEn
                        }

                        ameOptionsLayout.addView(optionView)
                    }

                    ameLayout.setOnClickListener {
                        if (ameOptionsLayout.visibility == View.GONE) {
                            ameOptionsLayout.expand()
                            ameArrow.animate().apply {
                                rotation(0f)
                                duration = 500
                            }.start()
                        } else {
                            ameOptionsLayout.collapse()
                            ameArrow.animate().apply {
                                rotation(-90f)
                                duration = 500
                            }.start()
                        }
                    }

                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )



        autoDispose.add(
            viewModel.filterItems("rest").observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    it.response.forEach { item ->
                        val optionView = LayoutInflater.from(context)
                            .inflate(R.layout.filter_option_item_layout, ameOptionsLayout, false)
                        optionView.findViewById<CheckBox>(R.id.optionCheck)
                            .setOnCheckedChangeListener { btn, state ->
                                viewModel.add2Rest(item.id)

                            }
                        optionView.setOnClickListener {
                            optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                        }
                        val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(Constants.LANG_KEY,"en")

                        if (lan.equals("ar")){
                            optionView.findViewById<TextView>(R.id.optionTitle).text = item.name
                        }else{
                            optionView.findViewById<TextView>(R.id.optionTitle).text = item.nameEn

                        }

                        facOptionsLayout.addView(optionView)
                    }

                    facLayout.setOnClickListener {
                        if (facOptionsLayout.visibility == View.GONE) {
                            facOptionsLayout.expand()
                            facLayoutArrow.animate().apply {
                                rotation(0f)
                                duration = 500
                            }.start()
                        } else {
                            facOptionsLayout.collapse()
                            facLayoutArrow.animate().apply {
                                rotation(-90f)
                                duration = 500
                            }.start()
                        }
                    }

                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )


        autoDispose.add(
            viewModel.filterItems("category").observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    it.response.forEach { item ->
                        val optionView = LayoutInflater.from(context)
                            .inflate(R.layout.filter_option_item_layout, ameOptionsLayout, false)

                        optionView.findViewById<CheckBox>(R.id.optionCheck)
                            .setOnCheckedChangeListener { btn, state ->
                                viewModel.add2Category(item.id)

                            }
                        optionView.setOnClickListener {
                            optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                        }
                        val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(Constants.LANG_KEY,"en")

                        if (lan.equals("ar")){
                            optionView.findViewById<TextView>(R.id.optionTitle).text = item.name
                        }else{
                            optionView.findViewById<TextView>(R.id.optionTitle).text = item.nameEn

                        }
                        propertyTypeOptionsLayout.addView(optionView)

                    }

                    propertyTypeLayout.setOnClickListener {
                        if (propertyTypeOptionsLayout.visibility == View.GONE) {
                            propertyTypeOptionsLayout.expand()
                            propertyTypeLayoutArrow.animate().apply {
                                rotation(0f)
                                duration = 500
                            }.start()
                        } else {
                            propertyTypeOptionsLayout.collapse()
                            propertyTypeLayoutArrow.animate().apply {
                                rotation(-90f)
                                duration = 500
                            }.start()
                        }
                    }


                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )

        pickDate.setOnClickListener {
            ReservationDatePickerFragment(onDatePickedListener =
            object :
                ReservationDatePickerFragment.OnDatePickedListener {
                override fun onDatePicked(startDateL: Long, endDateL: Long) {
                    val dateStart: String =
                        SimpleDateFormat("yyyy-MM-dd").format(startDateL)
                    val dateSec: String =
                        SimpleDateFormat("yyy-MM-dd").format(endDateL)
                    startDate.text = dateStart
                    endDate.text = dateSec
//
//                    Log.d("datelog",dateStart)
//                    Log.d("datelog",dateSec)
                    viewModel.addFilterDate(dateStart, dateSec)
                }
            }).show(
                activity!!.supportFragmentManager,
                ""
            )
        }

        adultsPlusBtn.setOnClickListener {
            var count = adultsCount.text.toString().toInt() + 1
            adultsCount.setText("$count")
        }
        adultsMinusBtn.setOnClickListener {
            var count = adultsCount.text.toString().toInt() - 1
            if (count >= 0)
                adultsCount.setText("$count")
        }

        childPlusBtn.setOnClickListener {
            var count = childCount.text.toString().toInt() + 1
            childCount.setText("$count")
        }
        childMinusBtn.setOnClickListener {
            var count = childCount.text.toString().toInt() - 1
            if (count >= 0)
                childCount.setText("$count")
        }


        bedsPlusBtn.setOnClickListener {
            var count = bedsCount.text.toString().toInt() + 1
            bedsCount.setText("$count")

        }

        bedsMinusBtn.setOnClickListener {
            var count = bedsCount.text.toString().toInt() - 1
            if (count >= 0)
                bedsCount.setText("$count")
        }


        roomsPlusBtn.setOnClickListener {
            var count = roomsCount.text.toString().toInt() + 1
            roomsCount.setText("$count")

        }

        roomsMinusBtn.setOnClickListener {
            var count = roomsCount.text.toString().toInt() - 1
            if (count >= 0)
                roomsCount.setText("$count")
        }

        bathPlusBtn.setOnClickListener {
            var count = bathCount.text.toString().toInt() + 1
            bathCount.setText("$count")
        }

        bathMinusBtn.setOnClickListener {
            var count = bathCount.text.toString().toInt() - 1
            if (count >= 0)
                bathCount.setText("$count")
        }


    }
}