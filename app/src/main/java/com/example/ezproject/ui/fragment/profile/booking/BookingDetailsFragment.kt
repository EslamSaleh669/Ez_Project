package com.example.ezproject.ui.fragment.profile.booking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ezproject.R
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.ui.fragment.profile.ProfileViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.bitmapDescriptorFromVector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_reservation_details.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class BookingDetailsFragment : Fragment(), OnMapReadyCallback {

    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var mMap: GoogleMap? = null
    var latLng: LatLng? = null
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
        return inflater.inflate(R.layout.fragment_reservation_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.selectedBooking?.let { booking ->
            bookingNumber.text = "Ezuru#${booking.id}"
            bookingStatus.text = when (booking.status) {
                -5 -> "UnPaid - Need Payment"
                -4 -> "Booking rejected"
                -3 -> "Cancel"
                -2 -> "Cancel Request"
                -1 -> "Closed - Expired"
                0 -> "Waiting  Approval"
                1 -> "Approved"
                2 -> "Paid"
                3 -> "Checkin"
                4 -> "Checkout"
                5 -> "Confirm Key Delivered"
                6 -> "Key Delivered"
                else -> "Accepted"

            }


            checkInDate.text = booking.dateStart
            checkOutDate.text = booking.dateEnd

            val isOwner = arguments?.getBoolean("p") ?: false

            if (isOwner) {
                detailsTitle.text = "Guest Details"
                sendMsgBtn.text = "Contact Guest"

                sendMsgBtn.setOnClickListener {
                    (activity as AppCompatActivity).supportFragmentManager.commit {
                        replace(R.id.fragmentContainer, ChatMessagesFragment().apply {
                            arguments = bundleOf(
                                Pair(Constants.OWNER_ID_KEY, booking.ownerId),
                                Pair(Constants.RECEIVER_ID_KEY, booking.userId),
                                Pair(Constants.UNIT_ID_KEY, booking.unit.id)
                            )
                            addToBackStack("")
                        })
                    }
                }

                autoDispose.add(
                    viewModel.userDetails(booking.userId)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                            username.text = it.response[0].name
                            Glide.with(activity!!)
                                .load("${Constants.STORAGE_URL}${it.response[0].avatar}")
                                .error(R.drawable.ic_account)
                                .placeholder(R.drawable.ic_account).apply(
                                    RequestOptions.bitmapTransform(CropCircleTransformation())
                                ).into(userImage)
                            userDescription.text = it.response[0].description
                            callBtn.setOnClickListener { v ->
                                if (it.response[0].mobile.isNotEmpty()) {
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.data = Uri.parse("tel:${it.response[0].mobile}")
                                    startActivity(intent)
                                }
                            }
                        }, {
                            Timber.e(it)
                        })
                )

            } else {
                detailsTitle.text = "Host Details"
                sendMsgBtn.text = "Contact Host"

                sendMsgBtn.setOnClickListener {
                    (activity as AppCompatActivity).supportFragmentManager.commit {
                        replace(R.id.fragmentContainer, ChatMessagesFragment().apply {
                            arguments = bundleOf(
                                Pair(Constants.OWNER_ID_KEY, booking.ownerId),
                                Pair(Constants.RECEIVER_ID_KEY, booking.userId),
                                Pair(Constants.UNIT_ID_KEY, booking.unit.id)
                            )
                            addToBackStack("")
                        })
                    }
                }

                autoDispose.add(
                    viewModel.userDetails(booking.ownerId)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                            username.text = it.response[0].name
                            userDescription.text = it.response[0].description
                            Glide.with(activity!!)
                                .load("${Constants.STORAGE_URL}${it.response[0].avatar}")
                                .error(R.drawable.ic_account)
                                .placeholder(R.drawable.ic_account).apply(
                                    RequestOptions.bitmapTransform(CropCircleTransformation())
                                ).into(userImage)
                            callBtn.setOnClickListener { v ->
                                if (it.response[0].mobile.isNotEmpty()) {
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.data = Uri.parse("tel:${it.response[0].mobile}")
                                    startActivity(intent)
                                }
                            }
                        }, {
                            Timber.e(it)
                        })
                )

            }
            latLng = LatLng(booking.unit.latitude.toDouble(), booking.unit.longitude.toDouble())


            bookingAmount.text = "${booking.price} ${viewModel.currency}"
        }
    }

    override fun onMapReady(gmap: GoogleMap?) {
        mMap = gmap
        try {
            mMap?.let {
                latLng?.let { latLng ->
                    val unitLocation = latLng

                    it.addMarker(
                        MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin))
                            .position(
                                unitLocation
                            )
                    )
                    it.moveCamera(CameraUpdateFactory.newLatLng(unitLocation))
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(unitLocation, 10f))
                }
            }

        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}