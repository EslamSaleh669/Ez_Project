package com.example.ezproject.ui.fragment.unit.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.FavouriteAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favourite.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class FavouriteFragment : Fragment(), FavouriteAdapter.OnFavCLickListener {
    @Inject
    @field:Named("favourite")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavouriteViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        autoDispose.add(viewModel.favourites().observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it.response.data.isNotEmpty()) {
                val favs: MutableSet<String> = mutableSetOf()
                for (unit in it.response.data) {
                    favs.add(unit.id.toString())
                }
                viewModel.addFavSet(favs)
                favRecycler.adapter = FavouriteAdapter(
                    activity,
                    it.response.data,
                    autoDispose,
                    viewModel.currentCurrency,
                    this
                ) {
                    viewModel.unitDetails(it)
                }
                favRecycler.layoutManager = LinearLayoutManager(context)
                noDataFound.visibility = View.GONE
            } else {
                noDataFound.visibility = View.VISIBLE
                favRecycler.visibility = View.GONE
            }
        }, {
            Timber.e(it)
            context?.handleApiError(it)
        }))
    }

    override fun onFavClicked(unitId: Int) {

        autoDispose.add(
            viewModel.removeFrmFav(unitId).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (it.status == Constants.STATUS_OK) {
                        context?.makeToast(getString(R.string.unit_removed))
                        viewModel.removeFromLocalFav(unitId)
                        (favRecycler.adapter as FavouriteAdapter).removeItem(unitId)
                    }
                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )
    }


}