package com.example.ezproject.ui.fragment.unit.details

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.ui.activity.auth.splash.SplashViewModel
import com.example.ezproject.ui.adapter.AmentiesAdapter
import com.example.ezproject.ui.adapter.FeesAdapter
import com.example.ezproject.ui.adapter.UnitAdapter
import com.example.ezproject.ui.adapter.UnitImagesSlider
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.ui.fragment.profile.EditProfileFragment
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.ui.fragment.unit.host.HostProfileFragment
import com.example.ezproject.ui.fragment.unit.reservation.ReservationDatePickerFragment
import com.example.ezproject.ui.fragment.unit.reservation.ReservationSummaryBottomDialog
import com.example.ezproject.ui.fragment.unit.reviews.UnitReviewFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_unit_details.*
import kotlinx.android.synthetic.main.fragment_unit_details.backBtn
import kotlinx.android.synthetic.main.fragment_unit_details.userImage
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class UnitDetailsFragment : Fragment(), OnMapReadyCallback,
    ReservationSummaryBottomDialog.OnReservationSummaryActionListener {
    companion object {
        fun instance(unitId: Int): UnitDetailsFragment {
            return UnitDetailsFragment().apply {

                arguments = bundleOf(Pair(Constants.UNIT_ID_KEY, unitId))
            }
        }
    }
    @Inject
    @field:Named("unitDetails")
    lateinit var viewModeFactory: ViewModelProvider.Factory

    @Inject
    @field:Named("splash")
    lateinit var viewModelFactory2: ViewModelProvider.Factory




    private val viewModel: UnitDetailsViewModel by lazy {
        ViewModelProvider(this, viewModeFactory).get(UnitDetailsViewModel::class.java)
    }

    private val viewModel2: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory2).get(SplashViewModel::class.java)
    }


    private val autoDispose = AutoDispose()

    var mMap: GoogleMap? = null

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
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_unit_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = context?.launchLoadingDialog()
        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                dialog?.dismiss()
                timer2.cancel() //this will cancel the timer of the system
            }
        }, 3000)
        backBtn.setOnClickListener {

                //activity?.onBackPressed()
//                activity?.supportFragmentManager?.commit {
//                    replace(R.id.fragmentContainer, AllUnitsFragment().apply {
//                    })}

        }
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)

        arguments?.getInt(Constants.UNIT_ID_KEY)?.let {
            viewModel.unitId = it
            if (viewModel.isInFav()) {
                likeBtn.setImageResource(R.drawable.ic_like_filled)
            } else {
                likeBtn.setImageResource(R.drawable.ic_white_like_empty)
            }



            autoDispose.add(
                viewModel.completeUnitDetails().observeOn(AndroidSchedulers.mainThread())
                 .subscribe(
                    { unit ->
                        loadingView.visibility = View.GONE
                        detailsView.visibility = View.VISIBLE
                       // dialog?.dismiss()

                        Log.d("outtttttt",unit.toString())
                        unitImageSlider.setSliderAdapter(
                            UnitImagesSlider(
                                activity,
                                unit.attachments
                            )
                        )
                        shareBtn.setOnClickListener {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${Constants.UNIT_SHARE_URL}${unit.id}")
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            startActivity(shareIntent)
                        }

                        amentiesRecycler.adapter = AmentiesAdapter(unit.amenities, activity)
                        amentiesRecycler.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


                        Log.d("outtttttt",unit.owner.name)
                        if (!unit.owner.name.equals("Tolip")){

                            if (unit.fees.size > 0) {
                            val filteredMap = unit.fees.filter { it.selectable.equals("required")  }

                            reqservtxt.visibility = View.VISIBLE

                            feesRecycler.adapter = FeesAdapter(filteredMap, activity)
                            feesRecycler.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        }

                        }
                        try {
                            mMap?.let {

                                if (unit.latitude == null || unit.longitude == null){
                                    context?.makeToast("no location")
                                }else{
                                    val unitLocation =
                                        LatLng(unit.latitude.toDouble(), unit.longitude.toDouble())
                                    it.addMarker(
                                        MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin))
                                            .position(
                                                unitLocation
                                            )
                                    )
                                    it.moveCamera(CameraUpdateFactory.newLatLng(unitLocation))
                                    it.moveCamera(CameraUpdateFactory.newLatLng(unitLocation))
                                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(unitLocation, 10f))
                                    locationText.text = unit.address
                                }

                            }
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                        reserveBtn.setOnClickListener {
                            if (unit?.daysList?.isNotEmpty() == true) {
                                ReservationDatePickerFragment(unit,
                                    object :
                                        ReservationDatePickerFragment.OnDatePickedListener {
                                        override fun onDatePicked(
                                            startDateL: Long,
                                            endDateL: Long
                                        ) {

                                            autoDispose.add(
                                                viewModel.checkUnitAvailable(
                                                   unit.id,
                                                    SimpleDateFormat(
                                                        "yyyy-MM-dd",
                                                        Locale.UK
                                                    ).format(
                                                        startDateL
                                                    ),
                                                    SimpleDateFormat(
                                                        "yyyy-MM-dd",
                                                        Locale.UK
                                                    ).format(
                                                        endDateL
                                                    )

                                                ).observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(
                                                        { av ->

                                                            if (av.avilable == 1) {
                                                                Log.d("errorrrrrav",startDateL.toString())
                                                                Log.d("errorrrrrav",endDateL.toString())
                                                                Log.d("errorrrrrav",av.toString())


                                                                activity?.let {
                                                                    (it as AppCompatActivity).supportFragmentManager.commit {
                                                                        replace(
                                                                            R.id.fragmentContainer,
                                                                            ReservationSummaryBottomDialog( startDateL,
                                                                                endDateL,
                                                                                unit,
                                                                                av
                                                                            )
                                                                        )
                                                                        addToBackStack("")
                                                                    }
                                                                }

//                                                                ReservationSummaryBottomDialog(
//                                                                    startDateL,
//                                                                    endDateL,
//                                                                    unit,
//                                                                    av,
//                                                                    this@UnitDetailsFragment
//                                                                ).show(
//                                                                    activity!!.supportFragmentManager,
//                                                                    ""
//                                                                )
                                                            } else {
                                                                context?.makeToast(getString(R.string.not_available_for_dates))
                                                            } 

                                                        },
                                                        {
                                                            Timber.e(it)
                                                            context?.handleApiError(it)
                                                            Log.d("errorrrrr",it.message)
                                                            context?.makeToast(getString(R.string.not_available_for_dates))

                                                        })
                                            )
                                        }
                                    }).show(
                                    activity!!.supportFragmentManager,
                                    ""
                                )
                            } else {
                                context?.makeToast(getString(R.string.unit_not_available_for_dates))

                            }
                        }
                        unitImageSlider.startAutoCycle()
                        bedsCount.text = "${unit.beds} Beds"
                        guestsCount.text = "${unit.guests} Guests"
                        bathCount.text = "${unit.bathrooms} Bathrooms"
                        roomsCount.text = "${unit.rooms} Room"
                        summaryPrice.text = "${unit.price.roundPrice()}${viewModel.currentCurrency}"


                        if (unit.type == 371 || unit.type == 24 || unit.type == 397) {
                            perwhat.text = getString(R.string.day)
                            perwhat1.text = getString(R.string.day)
                        } else {
                            perwhat.text = getString(R.string.night)
                            perwhat1.text = getString(R.string.night)

                        }
                        unitTitle.text = unit.title
                        unitPrice.text = "${unit.price.roundPrice()} ${viewModel.currentCurrency}"
                        if (unit.description != null && unit.description.length >= 100) {
                            unitDescription.text = unit.description.substring(0, 100)
                            descriptionReadMore.setOnClickListener {
                                if (unitDescription.text.length < 101) {
                                    unitDescription.text = unit.description
                                    descriptionReadMore.text = getString(R.string.read_less)
                                } else {
                                    unitDescription.text = unit.description.substring(0, 100)
                                    descriptionReadMore.text = getString(R.string.read_more)
                                }
                            }

                        } else {
                            unitDescription.text = unit.description
                            descriptionReadMore.visibility = View.GONE
                        }

                        unitTotalRate.text = "${unit.rateScore}"
                        unitTotalRateCount.text = "(${unit.rateCount})"
                        unitRate.rating = unit.rateScore
                        checkInTime.text = "${getString(R.string.check_in)}${unit.checkin}"
                        checkOutTime.text = "${getString(R.string.check_out)}${unit.checkout}"

                        summaryRatingBar.rating = unit.rateScore
                        summaryRateCount.text = "(${unit.rateCount})"
                        summaryRateScore.text = unit.rateScore.toString()


                        hostDetailsBtn.setOnClickListener {
                            activity?.supportFragmentManager?.commit {
                                replace(R.id.fragmentContainer, HostProfileFragment().apply {
                                    arguments = bundleOf(Pair(Constants.USER_KEY, unit.userId))
                                })
                                addToBackStack("")
                            }
                        }

                        sendMsgBtn.setOnClickListener {
                            activity?.supportFragmentManager?.commit {
                                replace(R.id.fragmentContainer, ChatMessagesFragment().apply {
                                    arguments = bundleOf(


//                                        Pair(Constants.USER_KEY, unit.userId),
                                        Pair(Constants.OWNER_ID_KEY, unit.owner.id),
                                        Pair(Constants.UNIT_ID_KEY, unit.id),
                                        Pair(Constants.RECEIVER_ID_KEY, viewModel2.userData()?.id)

                                    )
                                    addToBackStack("")
                                })
                            }
                        }

                        if (unit.reviews.isNotEmpty()) {
                            unit.reviews.first().let {
                                Glide.with(activity!!)
                                    .load("${Constants.STORAGE_URL}${it.guest.avatar}")
                                    .placeholder(R.drawable.ic_account)
                                    .error(R.drawable.ic_account)
                                    .circleCrop()
                                    .into(reviewUserImage)


                                reviewUsername.text = it.guest.name
                                reviewContent.text = it.review
                                reviewRating.rating = it.score
                            }
                        } else {
                            reviewsLayout.visibility = View.GONE
                        }


                        /*
                        autoDispose.add(
                            viewModel.singleReview().observeOn(AndroidSchedulers.mainThread()).subscribe(
                                {
                                    if (it.response.isEmpty()) {
                                        reviewsLayout.visibility = View.GONE
                                    }
                                    Glide.with(activity!!)
                                        .load("${Constants.STORAGE_URL}${it.response[0].guest.avatar}")
                                        .apply(
                                            RequestOptions.bitmapTransform(
                                                CropCircleWithBorderTransformation()
                                            )
                                        ).into(reviewUserImage)


                                    reviewUsername.text = it.response[0].guest.name
                                    reviewContent.text = it.response[0].review
                                    reviewRating.rating = it.response[0].score
                                },
                                {
                                    Timber.e(it)
                                    context?.handleApiError(it)
                                })
                        )*/

                        readAllReviews.setOnClickListener {
                             activity?.supportFragmentManager?.commit {
                                replace(R.id.fragmentContainer, UnitReviewFragment().apply {
                                    arguments = bundleOf(Pair(Constants.UNIT_ID_KEY, unit.id))
                                })
                                addToBackStack("")
                            }
                        }
                        likeBtn.setOnClickListener {

                            if (viewModel2.userData()?.mobileVerifiedAt.equals(null) ||
                                viewModel2.userData()?.emailVerifiedAt.equals(null)||
                                viewModel2.userData()?.photoidVerifiedAt.equals(null)
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.you_must),
                                    Toast.LENGTH_LONG
                                ).show()

                                activity?.supportFragmentManager?.commit {
                                    replace(R.id.fragmentContainer, EditProfileFragment().apply {
                                    })
                                }
                            } else {

                                autoDispose.add(
                                    viewModel.addUnit2WishList()
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                            {
                                                if (it.status == Constants.STATUS_OK) {
                                                    viewModel.addUnit2LocalWishList()
                                                    it.response?.let {
                                                        likeBtn.setImageResource(R.drawable.ic_white_like_filled)
                                                        context?.makeToast(getString(R.string.unit_added_to_wish))
                                                    } ?: run {
                                                        likeBtn.setImageResource(R.drawable.ic_white_like_empty)
                                                        context?.makeToast(getString(R.string.unit_removed_from_wishlist))

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
                        autoDispose.add(
                            viewModel.similarUnits().observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        similarRecycler.adapter =
                                            UnitAdapter(
                                                it.response.units,
                                                viewModel.currentCurrency,
                                                activity, 2
                                            )
                                        similarRecycler.layoutManager = LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.HORIZONTAL, false
                                        )
                                    },
                                    {
                                        Timber.e(it)
                                        context?.handleApiError(it)
                                    })
                        )

                        readHouseRules.setOnClickListener {
                            Dialog(context!!).apply {
                                setContentView(R.layout.house_rules_dialog)
                                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                if (unit.allowChildrens == 1) {
                                    this.findViewById<Switch>(R.id.allowChild).isChecked = true
                                }

                                if (unit.allowInfants == 1) {
                                    this.findViewById<Switch>(R.id.allowCInfants).isChecked = true
                                }
                                if (unit.allowAnimals == 1) {
                                    this.findViewById<Switch>(R.id.allowAnimals).isChecked = true
                                }
                                if (unit.allowExtra == 1) {
                                    this.findViewById<Switch>(R.id.allowExtra).isChecked = true
                                }
                            }.show()
                        }

                        reportBtn.setOnClickListener {

                            Dialog(context!!).apply {
                                setContentView(R.layout.send_report_dialog)
                                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                this.findViewById<ImageView>(R.id.sendBtn).setOnClickListener {
                                    dismiss()
                                    val desc =
                                        this.findViewById<EditText>(R.id.reportText).text.toString()
                                            .trim()
                                    autoDispose.add(
                                        viewModel.reportUnit(desc)
                                            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                {
                                                    if (it.status == Constants.STATUS_OK) {
                                                        context.makeToast("Done reporting this unit")
                                                    } else {
                                                        context.makeToast("An error occured")
                                                    }
                                                },
                                                {
                                                    Timber.e(it)
                                                    context?.handleApiError(it)
                                                })
                                    )
                                }
                            }.show()

                        }

                        readCancellationPolicy.setOnClickListener {


                            autoDispose.add(
                                viewModel.gettheCancelationPolicy(if (unit.cpolicy.name.isNotEmpty()) unit.cpolicy.nameEn else "undefined")
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe(

                                        {

                                            if (it.data.equals(null)) {
                                                context?.makeToast("An error occured")

                                            } else {

                                                Dialog(context!!).apply {
                                                    setContentView(R.layout.cancelation_policy_dialog_layout)
                                                    window?.setBackgroundDrawable(
                                                        ColorDrawable(
                                                            Color.TRANSPARENT
                                                        )
                                                    )


                                                    this.findViewById<TextView>(R.id.policy_title).text =
                                                        if (unit.cpolicy.name.isNotEmpty()) unit.cpolicy.name else "undefined"
                                                    this.findViewById<TextView>(R.id.mydata).text =
                                                        it.data.toString()
                                                    this.findViewById<ImageView>(R.id.polbackBtn)
                                                        .setOnClickListener {
                                                            dismiss()
                                                        }

                                                }.show()
                                            }
                                        },
                                        {
                                            Timber.e(it)

                                            context?.handleApiError(it)
                                        })
                            )


                        }

//                        autoDispose.add(
//                            viewModel.cancelPolicy(unit.canclePolicy).subscribeOn(
//                                AndroidSchedulers.mainThread()
//                            ).subscribe({ response ->
//
//                            }, {
//                                Timber.e(it)
//                                context?.handleApiError(it)
//                            })
//                        )

//
//


                        Glide.with(activity!!).load("${Constants.STORAGE_URL}${unit.owner.avatar}")
                            .placeholder(R.drawable.ic_account)
                            .error(R.drawable.ic_account)
                            .circleCrop()
                            .into(userImage)
                        username.text = unit.owner.name

                    },
                    {
                        Timber.e(it)
                        context?.handleApiError(it)
                    })
            )
        }
    }

    override fun onMapReady(gmap: GoogleMap?) {
        mMap = gmap
    }


    override fun OnReservationSummaryAction(actionId: Int) {

        if (actionId == R.id.reportBtn) {

            Dialog(context!!).apply {
                setContentView(R.layout.send_report_dialog)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                this.findViewById<ImageView>(R.id.sendBtn).setOnClickListener {
                    dismiss()
                    val desc =
                        this.findViewById<EditText>(R.id.reportText).text.toString()
                            .trim()
                    autoDispose.add(
                        viewModel.reportUnit(desc).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                    if (it.status == Constants.STATUS_OK) {
                                        context.makeToast("Done reporting this unit")
                                    } else {
                                        context.makeToast("An error occured")
                                    }
                                },
                                {
                                    Timber.e(it)
                                    context?.handleApiError(it)
                                })
                    )
                }
            }.show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.VISIBLE


    }
}



/*
                        reserveBtn.setOnClickListener {
                            val builder = MaterialDatePicker.Builder.dateRangePicker()
                            val picker = builder.build()
                            picker.show(activity?.supportFragmentManager!!, picker.toString())
                            picker.addOnCancelListener {
                            picker.addOnCancelListener {
                                picker.dismiss()
                            }
                            picker.addOnPositiveButtonClickListener {
                                val dateStart: String =
                                    SimpleDateFormat("yyyy-M-dd").format(it.first)
                                val dateSec: String =
                                    SimpleDateFormat("yyy-M-dd").format(it.second)
                                autoDispose.add(
                                    viewModel.checkUnitAvailable(
                                        unit.id,
                                        dateStart,
                                        dateSec
                                    ).observeOn(AndroidSchedulers.mainThread()).subscribe({ av ->
                                        if (av.avilable != 0f) {
                                            ReservationSummaryBottomDialog(
                                                it.first!!,
                                                it.second!!,
                                                unit,
                                                av
                                            ).show(
                                                activity!!.supportFragmentManager,
                                                ""
                                            )
                                        } else {
                                            context?.makeToast("Not available for selected dates")
                                        }

                                    }, {
                                        Timber.e(it)
                                        context?.handleApiError(it)
                                        context?.makeToast("Not available for selected dates")

                                    })
                                )
                            }
                        }
*/



