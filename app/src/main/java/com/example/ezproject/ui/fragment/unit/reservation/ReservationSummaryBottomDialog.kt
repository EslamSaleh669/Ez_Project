package com.example.ezproject.ui.fragment.unit.reservation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.models.*
import com.example.ezproject.ui.adapter.OpionalServicesAdapter
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import com.example.ezproject.util.extensions.roundPrice
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_reservation_summary.*
import kotlinx.android.synthetic.main.select_paymrnt_method.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList


class ReservationSummaryBottomDialog(
    private val startDateL: Long,
    private val endDateL: Long,
    private val unit: UnitDetails,
    private val avgPrice: UnitAvailability2
) : Fragment(), OpionalServicesAdapter.OnOptionalServClick {
    @Inject
    @field:Named("reservation")
    lateinit var viewModeFactory: ViewModelProvider.Factory


    val SelectedServices = arrayListOf<OptionalServicesRef>()
    val FeesIds = arrayListOf<Int>()

//
//    @Inject
//    @field:Named("splash")
//    lateinit var viewModelFactory2: ViewModelProvider.Factory
//
//    private val viewModel2: SplashViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory2).get(SplashViewModel::class.java)
//    }

    private val viewModel: ReservationViewModel by lazy {
        ViewModelProvider(this, viewModeFactory).get(ReservationViewModel::class.java)
    }


    @Inject
    @field:Named("unitDetails")
    lateinit var viewModeFactory2: ViewModelProvider.Factory

    private val viewModel2: UnitDetailsViewModel by lazy {
        ViewModelProvider(this, viewModeFactory2).get(UnitDetailsViewModel::class.java)
    }



    private val autoDispose = AutoDispose()

    private var paymentMethod = "paypal"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialog)
        autoDispose.bindTo(this.lifecycle)
        (activity?.application as MyApplication).appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_reservation_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        startDate.text = SimpleDateFormat("MMM d", Locale.UK).format(startDateL)
        startDay.text = SimpleDateFormat("E", Locale.UK).format(startDateL)

        endDate.text = SimpleDateFormat("MMM d", Locale.UK).format(endDateL)
        endDay.text = SimpleDateFormat("E", Locale.UK).format(endDateL)

        val days: Int = ((endDateL - startDateL) / (24 * 60 * 60 * 1000)).toInt()



        nightsCount.text =
            "${avgPrice.unitTaxFee!!.allRentSeprate!!.dayPrice} *  ${ avgPrice.unitTaxFee.daysCount.toString()}  ${avgPrice.daysType}"

        nightPrice.text =
            "${avgPrice.unitTaxFee.rent!!.roundPrice()} ${viewModel.currentCurrency}"

        totalPrice.text = "${avgPrice.unitTaxFee.allRent!!.roundPrice()} ${viewModel.currentCurrency}"


//        if (avgPrice.unitTaxFee.fee[0].fee.taxonomy.nameEn< 1){
//            serviceFees.text = "${avgPrice.unitTaxFee.allRentSeprate.fee.roundPrice()} ${viewModel.currentCurrency}"
//
//        }else{
//            serviceFees.text = "${avgPrice.unitTaxFee.allRentSeprate.fee.roundPrice()} ${viewModel.currentCurrency}"
//            serviceFeeslinear.visibility = View.VISIBLE
//        }


        if (avgPrice.unitTaxFee.allRentSeprate!!.fee!! > 0) {

            avgPrice.unitTaxFee.requiredFee!!.forEach { item ->
                val optionView = LayoutInflater.from(context)
                    .inflate(R.layout.detailed_item_layout, myrequreditemsLayout, false)

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
                myrequreditemsLayout.addView(optionView)
            }

        }

//        serviceFees.text =
//            "${avgPrice.unitTaxFee.allRentSeprate.fee.roundPrice()} ${viewModel.currentCurrency}"
        ezuruFees.text =
            "${avgPrice.unitTaxFee.allRentSeprate.ezuru!!.roundPrice()} ${viewModel.currentCurrency}"


        unitmaxchilds.text = unit.maxChildrens.toString()
        unitmaxadults.text = unit.guests.toString()
//        tourismFees.text =
//            "${avgPrice.unitTaxFee.allRentSeprate.tourism.roundPrice()} ${viewModel.currentCurrency}"
//        vatFees.text =
//            "${avgPrice.unitTaxFee.allRentSeprate.vat.roundPrice()} ${viewModel.currentCurrency}"


        avgPrice.unitTaxFee.allOptionalFees.let {

            for (pos in it!!) {

           //     val obj = AllOptionalFeesItem(pos!!.feeId!!, "1", false)
                if (SelectedServices.any{ it.feeId == pos!!.feeId}){
                    Log.d("contannnnn","it contails valuse ${ pos!!.feeId}")
                }else{
                    val obj = OptionalServicesRef(
                        pos!!.amount!!.roundPrice(),pos.updatedAt,pos.selectable,pos.feeId,pos.createdAt,pos.id
                        ,pos.feeType, pos.taxonomy!!.name,pos.taxonomy.nameEn,pos.unitId,pos.peopleLimit,"1",false)
                    SelectedServices.add(obj)
                }



            }


        }





        optservRecycler.adapter = OpionalServicesAdapter(
            SelectedServices,
            activity,
            this
        )
        optservRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        nextstepBtn.setOnClickListener {

            val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences(
                Constants.SelectedFees,
                0
            )
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()


            val feesItem = FeesItem()
            val list = ArrayList<FeesItem>()
            val feesListRequest = FeesListRequest()


            for (i in SelectedServices) {
                if (i.checked == true) {

                    feesItem.feeId = i.feeId.toString()
                    feesItem.peopleLimit = i.count
                    list.add(feesItem)

                }
            }

            feesListRequest.unitId = unit.id.toString()
            feesListRequest.dateStart = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(startDateL)
            feesListRequest.dateEnd = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(startDateL)
            feesListRequest.fees = list


            Log.d("myouttttttttt",list.toString())
            autoDispose.add(
                viewModel2.checkUnitAvailablefee(
                    feesListRequest
                ).subscribeOn(Schedulers.io()).subscribe({

                    if (it.avilable != 0) {

                        activity?.supportFragmentManager?.commit {
                            replace(R.id.fragmentContainer,
                                SelectPaymentMethod(
                                    startDateL,
                                    endDateL,
                                    unit,
                                    it
                                ).apply {
                                })
                            addToBackStack("")

                        }

                    } else {
                        context?.makeToast(getString(R.string.not_available_for_dates))
                    }

                    Log.d("myerrorrs", it.toString())

                }, {
                    Timber.e(it)
                    context?.handleApiError(it)
                    Log.d("myerrorrs", it.message)
                })

            )

        }
//        reportBtn.setOnClickListener {
//            onReservationSummaryActionListener.OnReservationSummaryAction(it.id)
//        }

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

    override fun ServCount(position: Int, count: String) {

        SelectedServices.find { it.feeId == position }!!.count = count

    }

    override fun ServCheck(feeid: Int, checked: Boolean, position: Int) {
        SelectedServices.find { it.feeId == feeid }?.checked = checked


    }
}