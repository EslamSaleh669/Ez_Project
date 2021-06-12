package com.example.ezproject.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.AllCorporateAdapter
import com.example.ezproject.ui.fragment.main.MainViewModel
import com.example.ezproject.ui.fragment.unit.filter.FilterViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.launchLoadingDialog
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_all_hotels.*
import kotlinx.android.synthetic.main.fragment_all_units.noDataFound
import kotlinx.android.synthetic.main.fragment_allcorporates.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class AllFilteredHotelsFragment : Fragment() {

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

       // viewModel.hotels = ReplaySubject.create()
//
//        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences( Constants.preferences_fileName,0)
//        val sharedIdValue = sharedPreferences.getInt("selid",0)
        val dialog = context?.launchLoadingDialog()

//        viewModel.setOriginId(sharedIdValue)
//        arguments?.getInt(Constants.HOTELID)?.let {
//            if (it != 0) {
//                viewModel.setOriginId(0)
//                viewModel.setHotelUser(it)
//                Log.d("hoteluser",it.toString())
//            }
//        }

//
//
//        gridclick.setOnClickListener(View.OnClickListener {
//
//            autoDispose.add(viewModel.hotels.observeOn(AndroidSchedulers.mainThread()).subscribe({
//                if (it.isEmpty() && viewModel.limit == 0) {
//                    noDataFound.visibility = View.VISIBLE
//                    hotelsRecycler.visibility = View.GONE
//                } else {
//                    if (it.isNotEmpty()) {
//                        Timber.d("Data Loaded")
//                        (hotelsRecycler.adapter as HotelAdapter).addHotels(it)
//
//                    } else {
//                        context?.makeToast("No more data")
//                    }
//                }
//            }, {
//                noDataFound.visibility = View.VISIBLE
//                hotelsRecycler.visibility = View.GONE
//                Timber.e(it)
//                context?.handleApiError(it)
//            }))
//            hotelsRecycler.adapter =
//                HotelAdapter(arrayListOf(), viewModel.currentCurrency, activity,2)
//            hotelsRecycler.layoutManager = GridLayoutManager(context, 2)
//
//        })
//
//        horionclick.setOnClickListener(View.OnClickListener {
//
//            autoDispose.add(viewModel.hotels.observeOn(AndroidSchedulers.mainThread()).subscribe({
//                if (it.isEmpty() && viewModel.limit == 0) {
//                    noDataFound.visibility = View.VISIBLE
//                    hotelsRecycler.visibility = View.GONE
//                } else {
//                    if (it.isNotEmpty()) {
//                        Timber.d("Data Loaded")
//                        (hotelsRecycler.adapter as HotelAdapter).addHotels(it)
//
//                    } else {
//                        context?.makeToast("No more data")
//                    }
//                }
//            }, {
//                noDataFound.visibility = View.VISIBLE
//                hotelsRecycler.visibility = View.GONE
//                Timber.e(it)
//                context?.handleApiError(it)
//            }))
//
//            hotelsRecycler.adapter =
//                HotelAdapter(arrayListOf(), viewModel.currentCurrency, activity,1)
//            hotelsRecycler.layoutManager = LinearLayoutManager(context)
//
//        })


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

//
//        autoDispose.add(viewModel.hotels.observeOn(AndroidSchedulers.mainThread()).subscribe({
//           dialog?.dismiss()
//            if (it.isEmpty() && viewModel.limit == 0) {
//                noDataFound.visibility = View.VISIBLE
//                hotelsRecycler.visibility = View.GONE
//            } else {
//                if (it.isNotEmpty()) {
//                    Timber.d("Data Loaded")
//                    (hotelsRecycler.adapter as HotelAdapter).addHotels(it)
//
//                } else {
//                   context?.makeToast("No more data")
//                }
//            }
//        }, {
//            noDataFound.visibility = View.VISIBLE
//            hotelsRecycler.visibility = View.GONE
//            Timber.e(it)
//            context?.handleApiError(it)
//        }))
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            hotelsRecycler.setOnScrollChangeListener { view, i, i2, i3, i4 ->
//                val lastPosition: Int =
//                    (hotelsRecycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                viewModel.checkForhotelsLoading(lastPosition)
//            }
//        }



     //viewModel.getFilteredHotels().observeOn(AndroidSchedulers.mainThread()).subscribe()
        autoDispose.add(
            viewModel.getFilteredHotels().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    dialog?.dismiss()


                    if (it.data.isEmpty()){
                        noDataFound.visibility = View.VISIBLE
                        corporateRecycler.visibility = View.GONE
                    }else if (it.data.isNotEmpty()){

                        hotelsRecycler.adapter =
                            AllCorporateAdapter(it.data, viewModel.currentCurrency, activity!!)
                        hotelsRecycler.layoutManager = GridLayoutManager(context, 2)
                        //page++
                    }else{
                        context?.makeToast("No more data")
                    }

                },
                {
                    Timber.e(it)
                    Log.d("errorhotel", it.toString())
                })
        )



    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearFilters()
        viewModel.disposable?.dispose()
        viewModel.page = 1
        viewModel.limit = 0
    }
}