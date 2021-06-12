package com.example.ezproject.ui.fragment.unit.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.ReviewsAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_reviews.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UnitReviewFragment : Fragment() {


    @Inject
    @field:Named("reviews")
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private val viewModel: UnitReviewsViewModel by lazy {
        ViewModelProvider(this, viewModeFactory).get(UnitReviewsViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.unitId = arguments?.getInt(Constants.UNIT_ID_KEY)
        viewModel.unitId?.let {
            autoDispose.add(
                viewModel.unitReviews().observeOn(AndroidSchedulers.mainThread()).subscribe(
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