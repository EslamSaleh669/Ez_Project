package com.example.ezproject.ui.fragment.unit.add

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.ezproject.R
import com.example.ezproject.util.extensions.bitmapDescriptorFromVector
import com.example.ezproject.util.extensions.makeToast
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.map_dialog_fragment.*
import timber.log.Timber


class MapDialogFragment(
    private val defaultLocation: LatLng?,
    private val onLocationPicked: (location: LatLng) -> Unit
) : DialogFragment(),
    OnMapReadyCallback {


    var mMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null
    var pickedLocation: LatLng? = null
    val AUTOCOMPLETE_REQUEST_CODE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment =
            (activity?.supportFragmentManager?.findFragmentById(R.id.my_map) as SupportMapFragment?)

        mapFragment?.getMapAsync(this)


        pickBtn.setOnClickListener {
            pickedLocation?.let {
                onLocationPicked(it)

                dismiss()
            } ?: context?.makeToast("Pick location")
        }

    }


    override fun onMapReady(gmap: GoogleMap?) {
        mMap = gmap
        var mapMarker: Marker? = null
        try {
            mMap?.let {
                Places.initialize(
                    activity?.application!!, activity!!.getString(R.string.googleplace_key)
                );

                val autocompleteFragment =
                    activity?.supportFragmentManager?.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment


                val fields = listOf(  Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG);

                autocompleteFragment.setPlaceFields(fields).setCountries("eg")


                autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                    override fun onPlaceSelected(place: Place) {
                        Timber.d("Place: ${place.latLng}")
                        place.latLng?.let {latLng->
                            mapMarker?.remove()
                            mapMarker = it.addMarker(
                                MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin)).position(
                                    latLng
                                )
                            )


                            pickedLocation = latLng
                            if (pickBtn.visibility == View.GONE) {

                                pickBtn.apply {
                                    alpha = 0f
                                    visibility = View.VISIBLE

                                    // Animate the content view to 100% opacity, and clear any animation
                                    // listener set on the view.
                                    animate()
                                        .alpha(1f)
                                        .setDuration(500)
                                        .setListener(null)

                                }
                            }
                        }
                    }

                    override fun onError(p0: Status) {
                        Timber.d("An error occurred: $p0")
                    }
                })

                val unitLocation = defaultLocation ?: LatLng(30.02, 31.13)
                mapMarker = it.addMarker(
                    MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin)).position(
                        unitLocation
                    )
                )
                 it.moveCamera(CameraUpdateFactory.newLatLngZoom(unitLocation, 10f))
                mMap?.setOnMapClickListener { latLng ->
                    mapMarker?.remove()
                    mapMarker = it.addMarker(
                        MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin)).position(
                            latLng
                        )
                    )

                    pickedLocation = latLng
                    if (pickBtn.visibility == View.GONE) {

                        pickBtn.apply {
                            alpha = 0f
                            visibility = View.VISIBLE

                            // Animate the content view to 100% opacity, and clear any animation
                            // listener set on the view.
                            animate()
                                .alpha(1f)
                                .setDuration(500)
                                .setListener(null)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapFragment?.let {
            activity?.supportFragmentManager?.beginTransaction()?.remove(it)?.commit()
        }
    }
}