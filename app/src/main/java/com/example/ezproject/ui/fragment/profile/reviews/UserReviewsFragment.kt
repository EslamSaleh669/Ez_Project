package com.example.ezproject.ui.fragment.profile.reviews

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.UserReviewsAdapter
import com.example.ezproject.ui.fragment.profile.ProfileViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_reviews.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UserReviewsFragment : Fragment() {

    @Inject
    @field:Named("userReview")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ReviewsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ReviewsViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()

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
        viewModel.iamOwner = arguments?.getBoolean("p") ?: false
        backBtn.setOnClickListener{
            activity?.onBackPressed()
        }
        if (viewModel.iamOwner) {
            title.text = "Units Reviews"
        } else {
            title.text = "My Reviews"
        }

        reviewsRecycler.adapter = UserReviewsAdapter(arrayListOf(), activity)
        reviewsRecycler.layoutManager = LinearLayoutManager(context)
        autoDispose.add(viewModel.reviews.observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it.isNotEmpty()) {
                (reviewsRecycler.adapter as UserReviewsAdapter).addItems(it)
            } else {
                if (reviewsRecycler.adapter?.itemCount ?: 0 == 0) {
                    noDataFound.visibility = View.VISIBLE
                    reviewsRecycler.visibility = View.GONE
                }
                context?.makeToast("No more data")
            }

        }, {
            Timber.e(it)
        }))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            reviewsRecycler.setOnScrollChangeListener { view, i, i2, i3, i4 ->
                val lastPosition =
                    (reviewsRecycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                viewModel.checkForLoadingData(lastPosition)
            }
        }

        viewModel.loadReviews()
    }
}