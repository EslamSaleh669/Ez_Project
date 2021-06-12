package com.example.ezproject.ui.fragment.unit.filter

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.models.SearchPlaces
import com.example.ezproject.ui.adapter.UnitAdapter
import com.example.ezproject.ui.fragment.main.MainViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.launchLoadingDialog
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.ReplaySubject
import kotlinx.android.synthetic.main.fragment_all_units.*
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AllUnitsFragment : Fragment() {

    @Inject
    @field:Named("filter")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FilterViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(FilterViewModel::class.java)
    }


    @Inject
    @field:Named("main")
    lateinit var viewModelFactory2: ViewModelProvider.Factory

    private val viewModel2: MainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory2).get(MainViewModel::class.java)
    }

    var selectedPlace: Int? = null

    private val autoDispose: AutoDispose = AutoDispose()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDispose.bindTo(this.lifecycle)
        (activity?.application as MyApplication).appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.units = ReplaySubject.create()

        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences( Constants.preferences_fileName,0)
        val sharedIdValue = sharedPreferences.getInt("selid",0)
        val dialog = context?.launchLoadingDialog()
        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                dialog?.dismiss()
                timer2.cancel()
            }
        }, 3000)
        viewModel.setOriginId(sharedIdValue)
        arguments?.getInt(Constants.CATE_ID_KEY)?.let {
            if (it != 0) {
                viewModel.setOriginId(0)
                viewModel.setCategoryId(it)
            }
        }

        unitsRecycler.adapter =
            UnitAdapter(arrayListOf(), viewModel.currentCurrency, activity,2)
        unitsRecycler.layoutManager = GridLayoutManager(context, 2)


        gridclick.setOnClickListener(View.OnClickListener {

            autoDispose.add(viewModel.units.observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.isEmpty() && viewModel.limit == 0) {
                    noDataFound.visibility = View.VISIBLE
                    unitsRecycler.visibility = View.GONE
                } else {
                    if (it.isNotEmpty()) {
                        Timber.d("Data Loaded")
                        (unitsRecycler.adapter as UnitAdapter).addUnits(it)

                    } else {
                        context?.makeToast("No more data")
                    }
                }
            }, {
                noDataFound.visibility = View.VISIBLE
                unitsRecycler.visibility = View.GONE
                Timber.e(it)
                context?.handleApiError(it)
            }))
            unitsRecycler.adapter =
                UnitAdapter(arrayListOf(), viewModel.currentCurrency, activity,2)
            unitsRecycler.layoutManager = GridLayoutManager(context, 2)

        })

        horionclick.setOnClickListener(View.OnClickListener {

            autoDispose.add(viewModel.units.observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.isEmpty() && viewModel.limit == 0) {
                    noDataFound.visibility = View.VISIBLE
                    unitsRecycler.visibility = View.GONE
                } else {
                    if (it.isNotEmpty()) {
                        Timber.d("Data Loaded")
                        (unitsRecycler.adapter as UnitAdapter).addUnits(it)

                    } else {
                        context?.makeToast("No more data")
                    }
                }
            }, {
                noDataFound.visibility = View.VISIBLE
                unitsRecycler.visibility = View.GONE
                Timber.e(it)
                context?.handleApiError(it)
            }))

            unitsRecycler.adapter =
                UnitAdapter(arrayListOf(), viewModel.currentCurrency, activity,1)
            unitsRecycler.layoutManager = LinearLayoutManager(context)

        })


        /*   autoDispose.add(
               viewModel.loadFilteredUnits().observeOn(AndroidSchedulers.mainThread()).subscribe(
                   {
                       if (it.response.units.isNotEmpty()) {


                       } else {
                           noDataFound.visibility = View.VISIBLE
                           unitsRecycler.visibility = View.GONE
                       }
                   },
                   {

                       noDataFound.visibility = View.VISIBLE
                       unitsRecycler.visibility = View.GONE
                       Timber.e(it)
                       context?.handleApiError(it)
                   })
           )*/


        autoDispose.add(viewModel.units.observeOn(AndroidSchedulers.mainThread()).subscribe({
           // dialog?.dismiss()
            if (it.isEmpty() && viewModel.limit == 0) {
                noDataFound.visibility = View.VISIBLE
                unitsRecycler.visibility = View.GONE
            } else {
                if (it.isNotEmpty()) {
                    Timber.d("Data Loaded")
                    (unitsRecycler.adapter as UnitAdapter).addUnits(it)

                } else {
                   context?.makeToast("No more data")
                }
            }
        }, {
            noDataFound.visibility = View.VISIBLE
            unitsRecycler.visibility = View.GONE
            Timber.e(it)
            context?.handleApiError(it)
        }))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            unitsRecycler.setOnScrollChangeListener { view, i, i2, i3, i4 ->
                val lastPosition: Int =
                    (unitsRecycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                viewModel.checkForUnitsLoading(lastPosition)
            }
        }

         viewModel.loadFilteredUnits()


        autoDispose.add(
            viewModel2.searchPlaces("").observeOn(AndroidSchedulers.mainThread()).subscribe(
                {

                    placesSearch1.setAdapter(
                        ArrayAdapter(
                            context!!,
                            R.layout.support_simple_spinner_dropdown_item,
                            it
                        )
                    )

                    placesSearch1.setOnItemClickListener { parent, view, position, id ->
//
                        var selectedid = parent.getItemAtPosition(position) as SearchPlaces
                        selectedPlace = selectedid.id
                        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                        editor.putInt("selid", selectedPlace!!)
                        editor.apply()
                    }

                },
                {

                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )


        filtersBtn1.setOnClickListener {

            if (selectedPlace == null){
                context!!.makeToast(getString(R.string.please_sel_place))
            }else{
                activity?.supportFragmentManager?.commit {
                    replace(R.id.fragmentContainer, FilterHotelsDialog())
                    addToBackStack("")
                }
            }
        }

        searchicon1.setOnClickListener {

            if (selectedPlace == null) {
                context?.makeToast(getString(R.string.please_sel_place))
            }else{

                viewModel.setFilterCounts(
                    "1",
                    "1",
                    "1",
                    "1",
                    "1",
                    selectedPlace
                )
                (activity as AppCompatActivity).supportFragmentManager.commit {
                    replace(
                        R.id.fragmentContainer,
                        AllUnitsFragment()
                    )
                    addToBackStack("")
                }
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearFilters()
        viewModel.disposable?.dispose()
        viewModel.page = 1
        viewModel.limit = 0
    }
}