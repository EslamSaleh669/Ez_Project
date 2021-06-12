package com.example.ezproject.ui.fragment

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.ezproject.ui.adapter.HotelAdapter
import com.example.ezproject.ui.fragment.main.MainViewModel
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.ui.fragment.unit.filter.FilterDialogFragment
import com.example.ezproject.ui.fragment.unit.filter.FilterHotelsDialog
import com.example.ezproject.ui.fragment.unit.filter.FilterViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.launchLoadingDialog
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.ReplaySubject
import kotlinx.android.synthetic.main.fragment_all_hotels.*
import kotlinx.android.synthetic.main.fragment_all_units.*
import kotlinx.android.synthetic.main.fragment_all_units.noDataFound
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AllHotelssFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_all_hotels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hotels = ReplaySubject.create()

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
        arguments?.getInt(Constants.HOTELID)?.let {
            if (it != 0) {
                viewModel.setOriginId(0)
                viewModel.setHotelUser(it)
                Log.d("hoteluser",it.toString())
            }
        }


        hotelsRecycler.adapter =
            HotelAdapter(arrayListOf(), viewModel.currentCurrency,activity,2)
        hotelsRecycler.layoutManager = GridLayoutManager(context, 2)



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


        autoDispose.add(viewModel.hotels.observeOn(AndroidSchedulers.mainThread()).subscribe({
              dialog?.dismiss()
            if (it.isEmpty() && viewModel.limit == 0) {
                noDataFound.visibility = View.VISIBLE
                hotelsRecycler.visibility = View.GONE
            } else {
                if (it.isNotEmpty()) {
                    Timber.d("Data Loaded")
                    (hotelsRecycler.adapter as HotelAdapter).addHotels(it)

                } else {
                   context?.makeToast("No more data")
                }
            }
        }, {
            noDataFound.visibility = View.VISIBLE
            hotelsRecycler.visibility = View.GONE
            Timber.e(it)
            context?.handleApiError(it)
        }))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hotelsRecycler.setOnScrollChangeListener { view, i, i2, i3, i4 ->
                val lastPosition: Int =
                    (hotelsRecycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                viewModel.checkForhotelsLoading(lastPosition)
            }
        }

         viewModel.loadFilteredHotels()




        filtershBtn1.setOnClickListener {


            viewModel.setHotelownerid(arguments?.getInt(Constants.HOTELID))
                activity?.supportFragmentManager?.commit {
                    replace(R.id.fragmentContainer, FilterHotelsDialog().apply {
                        arguments = bundleOf(Pair(Constants.FHOTELID,arguments?.getInt(Constants.HOTELID)))

                    })
                    addToBackStack("")
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