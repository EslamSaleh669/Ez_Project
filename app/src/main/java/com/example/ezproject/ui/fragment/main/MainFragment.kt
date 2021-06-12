package com.example.ezproject.ui.fragment.main
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.SearchPlaces
import com.example.ezproject.ui.adapter.CategoryAdapter
import com.example.ezproject.ui.adapter.HotelPopularPAdapter
import com.example.ezproject.ui.adapter.TouristPlacesAdapter
import com.example.ezproject.ui.fragment.AllHotelssFragment
import com.example.ezproject.ui.fragment.profile.EditProfileFragment
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment
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
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MainFragment : Fragment(){


    @Inject
    @field:Named("filter")
    lateinit var viewModelFactory2: ViewModelProvider.Factory

    private val viewMode2: FilterViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory2).get(FilterViewModel::class.java)
    }

    @Inject
    @field:Named("main")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }



    private val autoDispose: AutoDispose = AutoDispose()
    var dialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDispose.bindTo(this.lifecycle)
        (activity?.application as MyApplication).appComponent?.inject(this)

    }

    var selectedPlace: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.VISIBLE

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(
            Constants.preferences_fileName,
            0
        )

        dialog = context?.launchLoadingDialog()

        val sharedPreferencess: SharedPreferences = context!!.getSharedPreferences( Constants.Private_or_Hotel,0)
        val sharedIdValue = sharedPreferencess.getInt("catidd",1)

        if (sharedIdValue == 1){
            getprstaydata()
            selectprivatestay.setBackgroundResource(R.drawable.selected_btn_background)
            selecthotel.setBackgroundResource(R.drawable.deselected_btn_backgound)
        }else{
            gethoteldata()
            selectprivatestay.setBackgroundResource(R.drawable.deselected_btn_backgound)
            selecthotel.setBackgroundResource(R.drawable.selected_btn_background)
        }





        selecthotel.setOnClickListener {

            dialog = context!!.launchLoadingDialog()
            selecthotel.setBackgroundResource(R.drawable.selected_btn_background)
            selectprivatestay.setBackgroundResource(R.drawable.deselected_btn_backgound)
            gethoteldata()
            val shpr: SharedPreferences = context!!.getSharedPreferences(
                Constants.Private_or_Hotel,
                0
            )
            val editor: SharedPreferences.Editor = shpr.edit()
            editor.putInt("catidd",2)
            editor.apply()


        }


        selectprivatestay.setOnClickListener {

            dialog = context!!.launchLoadingDialog()
            selectprivatestay.setBackgroundResource(R.drawable.selected_btn_background)
            selecthotel.setBackgroundResource(R.drawable.deselected_btn_backgound)
            getprstaydata()
            val shpr2: SharedPreferences = context!!.getSharedPreferences(
                Constants.Private_or_Hotel,
                0
            )
            val editor: SharedPreferences.Editor = shpr2.edit()
            editor.putInt("catidd",1)
            editor.apply()


        }
//
//
//        val uri = activity?.intent?.data
//        if (uri != null) {
//            activity.let {
//                (it as AppCompatActivity).supportFragmentManager.commit {
//                    replace(
//                        R.id.fragmentContainer,
//                        UnitDetailsFragment.instance(1437)
//                    )
//                    addToBackStack("")
//                }
//            }
//        }


        viewModel.userData()?.let {
            username.text = getString(R.string.welcome_user, it.name)

            Glide.with(activity!!).load("${Constants.STORAGE_URL}${it.avatar}")
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .circleCrop()
                .into(userImage)

            userImage.setOnClickListener {
                activity?.supportFragmentManager?.commit {
                    replace(R.id.fragmentContainer, EditProfileFragment())
                    addToBackStack("")
                }
            }
        }

        autoDispose.add(
            viewModel.searchPlaces("").observeOn(AndroidSchedulers.mainThread()).subscribe(
                {

                    placesSearch.setAdapter(
                        ArrayAdapter(
                            context!!,
                            R.layout.support_simple_spinner_dropdown_item,
                            it
                        )
                    )

                    placesSearch.setOnItemClickListener { parent, view, position, id ->
//
                        var selectedid = parent.getItemAtPosition(position) as SearchPlaces
                        selectedPlace = selectedid.id
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putInt("selid", selectedPlace!!)
                        editor.apply()
                    }

                },
                {

                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )

        filtersBtn.setOnClickListener {

            val xsharedPreferencess: SharedPreferences = context!!.getSharedPreferences( Constants.Private_or_Hotel,0)
            val sharedIdValue = xsharedPreferencess.getInt("catidd",1)

            if (selectedPlace == null){
                context!!.makeToast(getString(R.string.please_sel_place))
            }else{
                if (sharedIdValue == 1){
                    activity?.supportFragmentManager?.commit {
                        replace(R.id.fragmentContainer, FilterDialogFragment())
                        addToBackStack("")
                    }
                }else{
                    activity?.supportFragmentManager?.commit {
                        replace(R.id.fragmentContainer, FilterHotelsDialog())
                        addToBackStack("")
                    }
                }

            }
        }

        searchicon.setOnClickListener {

            val xxsharedPreferencess: SharedPreferences = context!!.getSharedPreferences( Constants.Private_or_Hotel,0)
            val sharedIdValue = xxsharedPreferencess.getInt("catidd",1)

            if (selectedPlace == null) {
                 context?.makeToast(getString(R.string.please_sel_place))
            }else{

                if (sharedIdValue == 1){
                    viewMode2.setFilterCounts(
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
                }else{
                    viewMode2.setFilterCounts(
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
                            AllHotelssFragment()
                        )
                        addToBackStack("")
                    }
                }


            }
        }
    }

    fun getprstaydata(){

        autoDispose.add(viewModel.categories().observeOn(AndroidSchedulers.mainThread()).subscribe({
            //   dialog?.dismiss()

            Log.d("catresp",it.toString())


            categoryRecycler.adapter = CategoryAdapter(it.response.categories, activity,1)
            categoryRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }, {
            Timber.e(it)
        }))

        autoDispose.add(
            viewModel.places("tourist_places").observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    dialog?.dismiss()

                    touristPlacesRecycler.adapter =
                        TouristPlacesAdapter(it.response.places, activity)
                    touristPlacesRecycler.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


                },
                {
                    Timber.e(it)
                })
        )
    }



    fun gethoteldata(){

        autoDispose.add(viewModel.categories_Hotels().observeOn(AndroidSchedulers.mainThread()).subscribe({
             dialog?.dismiss()

            categoryRecycler.adapter = CategoryAdapter(it.response.categories, activity,2)
            categoryRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }, {
            Timber.e(it)
        }))

        autoDispose.add(
            viewModel.hotelpopuardestnations().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    //  dialog?.dismiss()

                    touristPlacesRecycler.adapter =
                        HotelPopularPAdapter(it, activity)
                    touristPlacesRecycler.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

                },
                {
                    Timber.e(it)
                })
        )
    }
}



//
//        autoDispose.add(
//            viewModel.featuredUnits().observeOn(AndroidSchedulers.mainThread()).subscribe(
//                {
//                    dialog?.dismiss()
//
//                    featuredUnitsRecycler.adapter =
//                        UnitAdapter(it.response.units, viewModel.currentCurrency, activity)
//                    featuredUnitsRecycler.layoutManager =
//                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//
//
//                },
//                {
//                    Timber.e(it)
//                    context?.handleApiError(it)
//
//                })
//        )
//
//        autoDispose.add(
//            viewModel.recommendedUnits().observeOn(AndroidSchedulers.mainThread()).subscribe(
//                {
//                    dialog?.dismiss()
//
//                    recommendedRecycler.adapter =
//                        UnitAdapter(it.response.units, viewModel.currentCurrency, activity)
//                    recommendedRecycler.layoutManager =
//                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                },
//                {
//                    Timber.e(it)
//                    context?.handleApiError(it)
//                })
//        )


//        autoDispose.add(
//            viewModel.places("popular_places").observeOn(AndroidSchedulers.mainThread()).subscribe(
//                {
//                    dialog?.dismiss()
//
//                    popularPlaces.adapter = TouristPlacesAdapter(it.response.places, activity)
//                    popularPlaces.layoutManager =
//                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                },
//                {
//                    Timber.e(it)
//                })
//        )
//
//        autoDispose.add(
//            viewModel.topExperienced().observeOn(AndroidSchedulers.mainThread()).subscribe(
//                {
//                    dialog?.dismiss()
//
//                    expRecycler.adapter = ExperianceAdapter(it.response.units, activity)
//                    expRecycler.layoutManager =
//                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                },
//                {
//                    Timber.e(it)
//                    context?.handleApiError(it)
//                })
//        )
//
