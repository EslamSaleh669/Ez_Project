package com.example.ezproject.ui.fragment

import android.app.Dialog
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
import com.example.ezproject.ui.adapter.AllCorporateAdapter
import com.example.ezproject.ui.adapter.HotelAdapter
import com.example.ezproject.ui.fragment.main.MainViewModel
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsViewModel
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
import kotlinx.android.synthetic.main.fragment_allcorporates.*
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AllCorporatesFragment : Fragment() {


    var page = 1 ;
    var dialog : Dialog? = null

    @Inject
    @field:Named("unitDetails")
    lateinit var viewModeFactory3: ViewModelProvider.Factory

    private val viewModel3: UnitDetailsViewModel by lazy {
        ViewModelProvider(this, viewModeFactory3).get(UnitDetailsViewModel::class.java)
    }


    @Inject
    @field:Named("filter")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FilterViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(FilterViewModel::class.java)
    }


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
        return inflater.inflate(R.layout.fragment_allcorporates, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val sharedPreferencess: SharedPreferences = context!!.getSharedPreferences( Constants.Private_or_Hotel,0)
        val sharedIdValuecatid = sharedPreferencess.getInt("categoryid",1)
        val sharedIdValuefromwhere = sharedPreferencess.getInt("myplace",1)

        if (arguments?.getInt(Constants.FROMWHERE) == 1){
            autoDispose.add(
                viewModel3.loadAllCorporates(page,arguments?.getInt(Constants.CATE_ID_KEY)!!).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        dialog?.dismiss()


                        if (it.data.isEmpty()){
                            noDataFound.visibility = View.VISIBLE
                            corporateRecycler.visibility = View.GONE
                        }else if (it.data.isNotEmpty()){
                            corporateRecycler.adapter =
                                AllCorporateAdapter(it.data, viewModel3.currentCurrency, activity!!)
                            corporateRecycler.layoutManager = GridLayoutManager(context, 2)
                            //page++
                        }else{

                            context?.makeToast("No more data")
                        }

                    },
                    {
                        Timber.e(it)
                    })
            )

        }

        else{
            autoDispose.add(
                viewModel3.loadAllCorporatesbydes(page,arguments?.getInt(Constants.CATE_ID_KEY)!!).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        dialog?.dismiss()

                        if (it.data.isEmpty()){
                            noDataFound.visibility = View.VISIBLE
                            corporateRecycler.visibility = View.GONE
                        }else if (it.data.isNotEmpty()){
                            corporateRecycler.adapter =
                                AllCorporateAdapter(it.data, viewModel.currentCurrency, activity!!)
                            corporateRecycler.layoutManager = GridLayoutManager(context, 2)
                          //  page++
                        }else{

                            context?.makeToast("No more data")
                        }


                    },
                    {
                        Timber.e(it)
                    })
            )
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