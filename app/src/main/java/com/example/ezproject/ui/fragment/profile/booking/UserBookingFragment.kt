package com.example.ezproject.ui.fragment.profile.booking

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.models.PayPalPayment
import com.example.ezproject.data.network.response.BookingResponse
import com.example.ezproject.ui.adapter.BookingAdapter
import com.example.ezproject.ui.adapter.MyUnitsAdapter
import com.example.ezproject.ui.fragment.profile.ProfileViewModel
import com.example.ezproject.ui.fragment.reviews.add.AddReviewFragment
import com.example.ezproject.ui.fragment.unit.reservation.ReservationPaymentFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_user_booking.*
import kotlinx.android.synthetic.main.fragment_user_booking.backBtn
import kotlinx.android.synthetic.main.fragment_user_unit.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UserBookingFragment : Fragment(),BookingAdapter.OnDelBookingCLickListener {
    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var isMyUnit: Boolean = false
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(ProfileViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_user_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        arguments?.getInt("p")?.let {
            title.text = getString(R.string.requests_for_my_unit)
            isMyUnit = true
        }
        autoDispose.add(
            loadBookings().subscribe(
                {
                    if (it.bookings.isNotEmpty()) {
                        bookingRecycler.adapter =
                            BookingAdapter(it.bookings, activity, isMyUnit, autoDispose, {
                                viewModel.unitDetails(it)
                            }, {
                                autoDispose.add(
                                    viewModel.updateBookingState(it)
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                            context?.makeToast("Done, updating booking")
                                        }, {
                                            Timber.e(it)
                                        })
                                )
                            }, { booking ->
                                activity?.supportFragmentManager?.commit {
                                    replace(
                                        R.id.fragmentContainer,
                                         AddReviewFragment().apply {
                                            arguments =
                                                bundleOf(
                                                    Pair(
                                                        Constants.UNIT_ID_KEY,
                                                        booking.unitId
                                                    ),
                                                    Pair(
                                                        Constants.UNIT_TITLE_KEY,
                                                        booking.unit.title
                                                    ),
                                                    Pair(
                                                        Constants.BOOKING_ID_KEY,
                                                        booking.id
                                                    ),
                                                    Pair(
                                                        Constants.OWNER_ID_KEY,
                                                        booking.ownerId
                                                    ),
                                                    Pair(
                                                        Constants.GUEST_ID_KEY,
                                                        booking.userId
                                                    ),
                                                    Pair(
                                                        Constants.START_DATE_KEY,
                                                        booking.dateStart
                                                    ),
                                                    Pair(
                                                        Constants.END_DATE_KEY,
                                                        booking.dateEnd
                                                    )
                                                )

                                            addToBackStack("")
                                        })
                                }
                                /*autoDispose.add(
                                    viewModel.canReviewUnit(booking.unitId)
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                            if (!it.result) {

                                            } else {
                                                context?.makeToast("You can not review this item")
                                            }
                                        }, {
                                            Timber.e(it)
                                        })
                                )*/
                            }, { booking ->
                                viewModel.selectedBooking = booking
                                (activity as AppCompatActivity).supportFragmentManager.commit {
                                    replace(R.id.fragmentContainer, BookingDetailsFragment().apply {
                                        arguments = bundleOf(Pair("p", isMyUnit))
                                    })
                                    addToBackStack("")
                                }
                            }, {

                                if (it.gateway == "paypal") {
                                    autoDispose.add(viewModel.loadPayPalInfo(it.id).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                        ReservationPaymentFragment(it.paypalLink).show(
                                            (activity as AppCompatActivity).supportFragmentManager, ""
                                        )

                                    },{
                                        Timber.e(it)
                                    }))

                                } else {
                                    ReservationPaymentFragment("https://backend.ezuru.net/api/payment/payfort/create/${it.id}").show(
                                        (activity as AppCompatActivity).supportFragmentManager, ""
                                    )


                            }
                    },this)
                    bookingRecycler.layoutManager = LinearLayoutManager(context)
                } else {
                noDataFound.visibility = View.VISIBLE
                bookingRecycler.visibility = View.GONE
            }
    },
    {
        Timber.e(it)
        context?.handleApiError(it)
    })
    )
}

private fun loadBookings(): Observable<BookingResponse> {
    return arguments?.getInt("p")?.let {
        viewModel.userPropertyBooking().observeOn(AndroidSchedulers.mainThread())
    } ?: viewModel.userBooking().observeOn(AndroidSchedulers.mainThread())

}

    override fun onDelClicked(bookingId: Int) {

        //activity!!.makeToast("this is my ID : ${bookingId}")
        val options = arrayOf<CharSequence>("Yes", "Cancel")
        val builder =
            AlertDialog.Builder(activity!!)
        builder.setTitle("Cancel this Booking!");
        builder.setItems(options) { dialog, item ->
            if (options[item].equals("Yes")) {

                autoDispose.add(
                    viewModel.deletebooking(bookingId).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            (bookingRecycler.adapter as BookingAdapter).removebookedItem(bookingId)

                            if (it.status == Constants.STATUS_OK) {
                                context?.makeToast(it.message!!)
                            }
                        },
                        {
                            Timber.e(it)
                            context?.handleApiError(it)
                            Log.d("errorresponse",it.message)
                        })
                )

            }  else if (options[item].equals("Cancel")) {
                dialog.dismiss()
            }
        }
        builder.show()

    }
    }
