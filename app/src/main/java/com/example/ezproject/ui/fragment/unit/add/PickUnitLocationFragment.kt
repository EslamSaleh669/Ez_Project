package com.example.ezproject.ui.fragment.unit.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.bitmapDescriptorFromVector
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_location.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class PickUnitLocationFragment : Fragment(), OnMapReadyCallback {


    @Inject
    @field:Named("unitDraft")
    lateinit var viewModeFactory: ViewModelProvider.Factory
    var mMap: GoogleMap? = null

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
        return inflater.inflate(R.layout.fragment_add_unit_location, container, false)
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
            val addressTxt = addressTxt.text.trim().toString()
            if (addressTxt.isNotEmpty()) {
                viewModel.unitDraft?.unit?.zipcode = zipCode.text.toString()
                viewModel.unitDraft?.unit?.buildingNumber = buildingNumber.text.toString()
                viewModel.unitDraft?.unit?.unitNumber = unitNumber.text.toString()
                viewModel.unitDraft?.unit?.floorNumber = floorNumber.text.toString()
                viewModel.unitDraft?.unit?.address = addressTxt
                autoDispose.add(
                    viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            if (it.status == 1) {
                                context?.makeToast("Done")
                            }
                            activity?.supportFragmentManager?.commit {
                                replace(R.id.fragmentContainer, PickUnitImagesFragment())
                                addToBackStack("")
                            }
                        },
                        {
                            Timber.e(it)
                            context?.handleApiError(it)
                        })
                )
            } else {
                context?.makeToast(getString(R.string.address_required))
            }
        }
    }

    private fun initView() {
        viewModel.unitDraft?.let {
            addressTxt.setText(viewModel.unitDraft?.unit?.address)
            buildingNumber.setText(viewModel.unitDraft?.unit?.buildingNumber)
            unitNumber.setText(viewModel.unitDraft?.unit?.unitNumber)
            floorNumber.setText(viewModel.unitDraft?.unit?.floorNumber)
            zipCode.setText(viewModel.unitDraft?.unit?.zipcode)
            (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
            countrySpinner.adapter = ArrayAdapter(
                context!!,
                R.layout.support_simple_spinner_dropdown_item,
                it.options.country.values.toList()
            )


            countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    adapter?.selectedItemPosition?.let { pos ->
                        viewModel.unitDraft?.unit?.country = it.options.country.keys.elementAt(pos)
                        autoDispose.add(
                            viewModel.governments(it.options.country.keys.elementAt(pos)).observeOn(
                                AndroidSchedulers.mainThread()
                            ).subscribe({ res ->
                                governmentSpinner.adapter = ArrayAdapter(
                                    context!!,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    res.response
                                )
                                governmentSpinner.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onNothingSelected(p0: AdapterView<*>?) {

                                        }

                                        override fun onItemSelected(
                                            p0: AdapterView<*>?,
                                            p1: View?,
                                            pos: Int,
                                            p3: Long
                                        ) {
                                            viewModel.unitDraft?.unit?.government =
                                                res.response[pos].id.toString()

                                            autoDispose.add(
                                                viewModel.cities(res.response[pos].id.toString()).observeOn(
                                                    AndroidSchedulers.mainThread()
                                                ).subscribe({ res ->
                                                    citySpinner.adapter = ArrayAdapter(
                                                        context!!,
                                                        R.layout.support_simple_spinner_dropdown_item,
                                                        res.response
                                                    )

                                                    citySpinner.onItemSelectedListener =
                                                        object :
                                                            AdapterView.OnItemSelectedListener {
                                                            override fun onNothingSelected(p0: AdapterView<*>?) {

                                                            }

                                                            override fun onItemSelected(
                                                                p0: AdapterView<*>?,
                                                                p1: View?,
                                                                pos: Int,
                                                                p3: Long
                                                            ) {
                                                                viewModel.unitDraft?.unit?.city =
                                                                    res.response[pos].id.toString()

                                                                autoDispose.add(
                                                                    viewModel.areas(res.response[pos].id.toString()).observeOn(
                                                                        AndroidSchedulers.mainThread()
                                                                    ).subscribe({ res ->
                                                                        areaSpinner.adapter =
                                                                            ArrayAdapter(
                                                                                context!!,
                                                                                R.layout.support_simple_spinner_dropdown_item,
                                                                                res.response
                                                                            )

                                                                        areaSpinner.onItemSelectedListener =
                                                                            object :
                                                                                AdapterView.OnItemSelectedListener {
                                                                                override fun onNothingSelected(
                                                                                    p0: AdapterView<*>?
                                                                                ) {

                                                                                }

                                                                                override fun onItemSelected(
                                                                                    p0: AdapterView<*>?,
                                                                                    p1: View?,
                                                                                    pos: Int,
                                                                                    p3: Long
                                                                                ) {
                                                                                    viewModel.unitDraft?.unit?.area =
                                                                                        res.response[pos].id.toString()
                                                                                }
                                                                            }

                                                                    }, {
                                                                        Timber.e(it)
                                                                        context?.handleApiError(it)
                                                                    })
                                                                )

                                                            }
                                                        }
                                                }, {
                                                    Timber.e(it)
                                                    context?.handleApiError(it)
                                                })
                                            )
                                        }
                                    }
                            }, {
                                Timber.e(it)
                                context?.handleApiError(it)
                            })
                        )

                    }
                }
            }
        }
    }


    override fun onMapReady(gmap: GoogleMap?) {
        mMap = gmap
        var mapMarker: Marker? = null
        try {
            mMap?.let {
                val unitLocation =
                    LatLng(
                        viewModel.unitDraft?.unit?.latitude?.toDouble() ?: 30.02,
                        viewModel.unitDraft?.unit?.longitude?.toDouble() ?: 31.13
                    )
                mapMarker = it.addMarker(
                    MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin)).position(
                        unitLocation
                    )
                )
                it.moveCamera(CameraUpdateFactory.newLatLng(unitLocation))
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(unitLocation, 10f))
                mMap?.setOnMapClickListener { latLng ->

                    MapDialogFragment(null) { picked ->
                        mapMarker?.remove()
                        viewModel.unitDraft?.unit?.latitude = picked.latitude.toString()
                        viewModel.unitDraft?.unit?.longitude = picked.longitude.toString()
                        mapMarker = it.addMarker(
                            MarkerOptions().icon(context?.bitmapDescriptorFromVector(R.drawable.ic_loc_pin)).position(
                                picked
                            )
                        )
                        it.moveCamera(CameraUpdateFactory.newLatLngZoom(picked, 10f))
                    }.show(activity?.supportFragmentManager!!, "")
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }


    }

}