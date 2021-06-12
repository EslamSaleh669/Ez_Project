package com.example.ezproject.ui.fragment.unit.reservation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.data.models.UnitAvailability2
import com.example.ezproject.data.models.UnitDetails
import com.example.ezproject.ui.activity.auth.splash.SplashViewModel
import com.example.ezproject.ui.fragment.profile.EditProfileFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.launchLoadingDialog
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.select_paymrnt_method.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class SelectPaymentMethod(
    private val startDateL: Long,
    private val endDateL: Long,
    private val unit: UnitDetails,
    private val avgPrice: UnitAvailability2

) : Fragment() {
    @Inject
    @field:Named("reservation")
    lateinit var viewModeFactory: ViewModelProvider.Factory


    @Inject
    @field:Named("splash")
    lateinit var viewModelFactory2: ViewModelProvider.Factory

    private val viewModel: ReservationViewModel by lazy {
        ViewModelProvider(this, viewModeFactory).get(ReservationViewModel::class.java)
    }


    private val viewModel2: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory2).get(SplashViewModel::class.java)
    }

    private val autoDispose = AutoDispose()
    private var paymentMethod = "paytabs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialog)
        autoDispose.bindTo(this.lifecycle)
        (activity?.application as MyApplication).appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_paymrnt_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        unittotalPrice.text = "${avgPrice.unitTaxFee!!.allRent} ${viewModel.currentCurrency}"
        unitrent.text = "${avgPrice.unitTaxFee.allRentSeprate!!.rent} ${viewModel.currentCurrency}"
        unitezuruservice.text = "${avgPrice.unitTaxFee.allRentSeprate.ezuru} ${viewModel.currentCurrency}"


        avgPrice.unitTaxFee.selectedOptionalFees!!.forEach { item ->
            val optionView = LayoutInflater.from(context)
                .inflate(R.layout.detailed_item_layout, selecteditemsLayout, false)

                 val name = optionView.findViewById<TextView>(R.id.itemTitle)
                 val price = optionView.findViewById<TextView>(R.id.itemprice)
                 val lan = activity?.getSharedPreferences(
                     Constants.SHARED_NAME,
                     Context.MODE_PRIVATE
                 )?.getString(Constants.LANG_KEY, "en")

            if (lan.equals("ar")){
                name.text = item!!.fee!!.taxonomy!!.name
                price.text = "${item.amount} ${viewModel.currentCurrency}"
            }else{
                name.text = item!!.fee!!.taxonomy!!.nameEn
                price.text = "${item.amount} ${viewModel.currentCurrency}"
            }

            selecteditemsLayout.addView(optionView)
        }


        avgPrice.unitTaxFee.requiredFee!!.forEach { item ->
            val optionView = LayoutInflater.from(context)
                .inflate(R.layout.detailed_item_layout, requreditemsLayout, false)

                 val name = optionView.findViewById<TextView>(R.id.itemTitle)
                 val price = optionView.findViewById<TextView>(R.id.itemprice)
                 val lan = activity?.getSharedPreferences(
                     Constants.SHARED_NAME,
                     Context.MODE_PRIVATE
                 )?.getString(Constants.LANG_KEY, "en")

            if (lan.equals("ar")){
                name.text = item!!.fee!!.taxonomy!!.name
                price.text = "${item.amount} ${viewModel.currentCurrency}"
            }else{
                name.text = item!!.fee!!.taxonomy!!.nameEn
                price.text = "${item.amount} ${viewModel.currentCurrency}"
            }
            requreditemsLayout.addView(optionView)
        }

        if (unit.allowChildrens == 1) {
            allowChildp.isChecked = true
        }

        if (unit.allowInfants == 1) {
            allowCInfantsp.isChecked = true
        }
        if (unit.allowAnimals == 1) {
            allowAnimalsp.isChecked = true
        }
        if (unit.allowExtra == 1) {
            allowExtrap.isChecked = true
        }

        startBookingBtn1.setOnClickListener {

            if (viewModel2.userData()?.mobileVerifiedAt.equals(null)
                ||viewModel2.userData()?.emailVerifiedAt.equals(null)
                || viewModel2.userData()?.photoidVerifiedAt.equals(null)

            ){
                Toast.makeText(context, getString(R.string.you_must), Toast.LENGTH_LONG).show()

                activity?.supportFragmentManager?.commit {
                    replace(R.id.fragmentContainer, EditProfileFragment().apply {
                    })
                    addToBackStack("")

                }


            }else{



            val dialog = context?.launchLoadingDialog()
            val timer2 = Timer()
            timer2.schedule(object : TimerTask() {
                override fun run() {
                    dialog?.dismiss()
                    timer2.cancel()
                }
            }, 3000)
            autoDispose.add(
                viewModel.addBooking(
                    unit.id,
                    unit.userId,
                    SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(startDateL),
                    SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(endDateL),
                    paymentMethod,
                    unit.guests,
                    unit.maxChildrens,
                    avgPrice.payPrice!!,
                    avgPrice.unitTaxFee.allRentSeprate.dayPrice!!,
                    avgPrice.unitTaxFee.allRentSeprate.fee!!,
                    avgPrice.unitTaxFee.allRentSeprate.ezuru!!,
                    avgPrice.unitTaxFee.allRentSeprate.tourism!!,
                    avgPrice.unitTaxFee.allRentSeprate.vat!!
                ).subscribeOn(Schedulers.io()).subscribe({
                    if (it.status == Constants.STATUS_OK) {
                        Log.d("paymentUrlll", it.response.toString())
//                            if (paymentMethod == "payfort") {
//                                "https://backend.ezuru.net/api/payment/payfort/create/${it.response.id}"
//                            } else

                        Log.d("paymentUrlll", it.response.paymentUrl)

                        it.response.paymentUrl
                            ?.let {
                                ReservationPaymentFragment(it).show(
                                    activity!!.supportFragmentManager,
                                    ""
                                )
                            }
                    }
                }, {
                    Timber.e(it)
                    context?.handleApiError(it)
                    Log.d("paymentUrlll", it.message)

                })
            )


            }


        }



//        Observable
//            .just(1, 2, 3, 4, 5, 6, 7, 8, 9)
//            .filter(object : Predicate<UnitDetails>() {
//                @Throws(Exception::class)
//
//
//                override fun test(t: UnitDetails): Boolean {
//                    TODO("Not yet implemented")
//                }
//            })
//            .subscribe(object : DisposableObserver<Int?>() {
//                fun onNext(integer: Int) {
//                    Log.e("ss", "Even: $integer")
//                }
//
//                fun onError(e: Throwable?) {}
//                fun onComplete() {}
//            })
        choosePaypale.setOnClickListener {
            choosePaypale.setImageResource(R.drawable.ic_paypal_active)
            choosePaytabs.setImageResource(R.drawable.ic_payfort_in_active)
            paymentMethod = "paypal"
        }

        choosePaytabs.setOnClickListener {
            choosePaypale.setImageResource(R.drawable.ic_paypal_in_active)
            choosePaytabs.setImageResource(R.drawable.ic_payfort_active)
            paymentMethod = "paytabs"

        }

    }


//    private fun loadUser() {
//        autoDispose.add(
//            viewModel2.userOnline().observeOn(AndroidSchedulers.mainThread()).subscribe(
//                { initViews(it) }, {
//                    context?.makeToast("no logged user")
//                    activity?.onBackPressed()
//                })
//        )
//    }

    interface OnReservationSummaryActionListener {
        fun OnReservationSummaryAction(actionId: Int)
    }
}


