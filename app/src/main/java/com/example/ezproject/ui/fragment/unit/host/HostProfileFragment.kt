package com.example.ezproject.ui.fragment.unit.host

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.ReviewsAdapter
import com.example.ezproject.ui.adapter.UnitAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.loadUrl
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import kotlinx.android.synthetic.main.fragment_host_profile.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class HostProfileFragment : Fragment() {
    @Inject
    @field:Named("host")
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private val viewModel: HostProfileViewModel by lazy {
        ViewModelProvider(this, viewModeFactory).get(HostProfileViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_host_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.hostId = arguments?.getInt(Constants.USER_KEY)
        viewModel.hostId?.let {
            autoDispose.add(
                viewModel.remoteUser().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {

                        Glide.with(activity!!)
                            .load("${Constants.STORAGE_URL}${it.response[0].avatar}")
                            .placeholder(R.drawable.ic_account)
                            .error(R.drawable.ic_account)
                            .apply(
                                RequestOptions.bitmapTransform(
                                    CropCircleWithBorderTransformation()
                                )
                            ).into(userImage)
                        userName.text = it.response[0].name
                        userLocation.text = it.response[0].city
                        overallRating.rating = it.response[0].rateScore
                        overallRatingScore.text = it.response[0].rateScore.toString()
                        overallRatingCount.text = it.response[0].rateCount.toString()
                    },
                    {
                        Timber.e(it)
                        context?.handleApiError(it)
                    })
            )

            autoDispose.add(viewModel.hostUnits().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    hostUnitsRecycler.adapter =
                        UnitAdapter(it.response.units, viewModel.currentCurrency, activity,2)
                    hostUnitsRecycler.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }, {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
            )


            autoDispose.add(
                viewModel.userReviews().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        reviewsRecycler.adapter = ReviewsAdapter(it.response, activity)
                        reviewsRecycler.layoutManager = LinearLayoutManager(context)
                    },
                    {
                        Timber.e(it)
                        context?.handleApiError(it)
                    })
            )


        }
    }
}