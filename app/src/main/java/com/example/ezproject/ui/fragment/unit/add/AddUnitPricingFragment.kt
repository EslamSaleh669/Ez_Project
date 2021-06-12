package com.example.ezproject.ui.fragment.unit.add

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.data.network.response.UnitDraftResponse
import com.example.ezproject.ui.activity.MainActivity
import com.example.ezproject.ui.fragment.profile.UserUnitsFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_pricing.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class AddUnitPricingFragment : Fragment() {

    @Inject
    @field:Named("unitDraft")
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private val viewModel: AddUnitViewModel by lazy {
        ViewModelProvider(activity!!, viewModeFactory).get(AddUnitViewModel::class.java)
    }

    private val autoDispose = AutoDispose()


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
        return inflater.inflate(R.layout.fragment_add_unit_pricing, container, false)
    }

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
            viewModel.unitDraft?.unit?.deliverKeys = keyDeliverInput.text.toString()
            viewModel.unitDraft?.unit?.getKeys = getKeyInput.text.toString()
            viewModel.unitDraft?.unit?.notes = notesInput.text.toString()
            viewModel.unitDraft?.unit?.status =
                if (viewModel.unitDraft?.unit?.status?.toInt() ?: 0 <= 0) "0" else "2"

            viewModel.unitDraft?.let {
                if (cleaningFees.text.trim().toString().isNotEmpty()) {
                    var cleaningFeesFound = false
                    for (fee in it.unit.fees) {
                        if (fee.id == "211") {
                            fee.amount = cleaningFees.text.trim().toString()
                            fee.feeType = (cleaningSpinner.selectedItem as String)
                            cleaningFeesFound = true
                            break
                        }
                    }
                    if (!cleaningFeesFound) {
                        it.unit.fees.add(
                            UnitDraftResponse.Fee(
                                cleaningFees.text.trim().toString(),
                                null,
                                "211",
                                (cleaningSpinner.selectedItem as String),
                                null,
                                null,
                                null
                            )
                        )
                    }

                }
            }


            viewModel.unitDraft?.let {
                if (parkingFees.text.trim().toString().isNotEmpty()) {
                    var parkingFeeFound = false
                    for (fee in it.unit.fees) {
                        if (fee.id == "212") {
                            fee.amount = parkingFees.text.trim().toString()
                            fee.feeType = (parkingSpinner.selectedItem as String)
                            parkingFeeFound = true
                            break
                        }
                    }
                    if (!parkingFeeFound) {
                        it.unit.fees.add(
                            UnitDraftResponse.Fee(
                                parkingFees.text.trim().toString(),
                                null,
                                "212",
                                (parkingSpinner.selectedItem as String),
                                null,
                                null,
                                null
                            )
                        )
                    }

                }
            }


            viewModel.unitDraft?.let {
                if (poolFees.text.trim().toString().isNotEmpty()) {
                    var poolFeeFound = false
                    for (fee in it.unit.fees) {
                        if (fee.id == "213") {
                            fee.amount = poolFees.text.trim().toString()
                            fee.feeType = (poolSpinner.selectedItem as String)
                            poolFeeFound = true
                            break
                        }
                    }
                    if (!poolFeeFound) {
                        it.unit.fees.add(
                            UnitDraftResponse.Fee(
                                poolFees.text.trim().toString(),
                                null,
                                "213",
                                (parkingSpinner.selectedItem as String),
                                null,
                                null,
                                null
                            )
                        )
                    }

                }
            }
            viewModel.unitDraft?.let {
                if (gymFees.text.trim().toString().isNotEmpty()) {
                    var gymFeeFound = false
                    for (fee in it.unit.fees) {
                        if (fee.id == "275") {
                            fee.amount = gymFees.text.trim().toString()
                            fee.feeType = (gymSpinner.selectedItem as String)
                            gymFeeFound = true
                            break
                        }
                    }
                    if (!gymFeeFound) {
                        it.unit.fees.add(
                            UnitDraftResponse.Fee(
                                gymFees.text.trim().toString(),
                                null,
                                "275",
                                (gymSpinner.selectedItem as String),
                                null,
                                null,
                                null
                            )
                        )
                    }

                }
            }
            autoDispose.add(
                viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.status == 1) {
                        context?.let {
                            Dialog(it, R.style.FullScreenDialog).apply {
                                setContentView(R.layout.fragment_reservation_done)
                                this.findViewById<TextView>(R.id.message).text = getString(R.string.unit_added)
                                show()
                                autoDispose.add(Observable.fromCallable {

                                }.delay(1, TimeUnit.SECONDS).subscribe {
                                    activity?.supportFragmentManager?.commit {
                                        replace(R.id.fragmentContainer, UserUnitsFragment())
                                        addToBackStack("")
                                    }
//                                    activity?.startActivity(
//                                        Intent(
//                                            activity!!,
//                                            MainActivity::class.java
//                                        ).apply {
//                                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        })
//                                    activity?.finish()
                                    dismiss()
                                })
                            }

                        }

                        }
                    },
                    {
                        Timber.e(it)
                        context?.handleApiError(it)
                    })
            )
        }

    }

    private fun initView() {
        currencyTxt.text = viewModel.currency
        parkingCurrencyTxt.text = viewModel.currency
        poolCurrencyTxt.text = viewModel.currency
        gymCurrencyTxt.text = viewModel.currency



        checkInInput.setText(viewModel.unitDraft?.unit?.checkin)
        checkoutInput.setText(viewModel.unitDraft?.unit?.checkout)

        checkInInput.setOnClickListener {
            val hoursInt = viewModel.unitDraft?.unit?.checkin?.split(":")?.get(0)?.toInt()
            val minInt = viewModel.unitDraft?.unit?.checkin?.split(":")?.get(1)?.toInt()
            TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { p0, hour, minute ->
                    checkInInput.setText("$hour : $minute")
                    viewModel.unitDraft?.unit?.checkin = "$hour:$minute"
                }, hoursInt ?: 15, minInt ?: 0, true
            ).show()
        }

        checkoutInput.setOnClickListener {
            val hoursInt = viewModel?.unitDraft?.unit?.checkout?.split(":")?.get(0)?.toInt()
            val minInt = viewModel?.unitDraft?.unit?.checkout?.split(":")?.get(1)?.toInt()
            TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { p0, hour, minute ->
                    checkoutInput.setText("$hour : $minute")
                    viewModel.unitDraft?.unit?.checkout = "$hour:$minute"
                }, hoursInt ?: 12, minInt ?: 0, true
            ).show()
        }

        viewModel.unitDraft?.let {
            for (fee in it.unit.fees) {
                if (fee.feeId == "211") {
                    cleaningFees.setText(fee.amount.toString())
                    if (fee.feeType == "fixed") {
                        cleaningSpinner.setSelection(0)
                    } else {
                        cleaningSpinner.setSelection(1)
                    }
                }

                if (fee.feeId == "212") {
                    parkingFees.setText(fee.amount.toString())
                    if (fee.feeType == "fixed") {
                        parkingSpinner.setSelection(0)
                    } else {
                        parkingSpinner.setSelection(1)
                    }
                }

                if (fee.feeId == "213") {
                    poolFees.setText(fee.amount.toString())
                    if (fee.feeType == "fixed") {
                        poolSpinner.setSelection(0)
                    } else {
                        poolSpinner.setSelection(1)
                    }
                }

                if (fee.feeId == "275") {
                    gymFees.setText(fee.amount.toString())
                    if (fee.feeType == "fixed") {
                        gymSpinner.setSelection(0)
                    } else {
                        gymSpinner.setSelection(1)
                    }
                }


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.VISIBLE

    }
}