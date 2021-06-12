package com.example.ezproject.ui.fragment.reviews.add

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.models.UserReview
import com.example.ezproject.ui.adapter.AddReviewItemsAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_review.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class AddReviewFragment : Fragment() {


    @Inject
    @field:Named("addReview")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddReviewViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(AddReviewViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unitTile.text = arguments?.getString(Constants.UNIT_TITLE_KEY)
        bookingDates.text =
            "${arguments?.getString(Constants.START_DATE_KEY)} to ${arguments?.getString(Constants.END_DATE_KEY)}"

        val localUserId = viewModel.localUser()?.id ?: 0
        val unitId = arguments?.getInt(Constants.UNIT_ID_KEY) ?: 0
        val bookingId = arguments?.getInt(Constants.BOOKING_ID_KEY) ?: 0
        val guestId = arguments?.getInt(Constants.GUEST_ID_KEY) ?: 0
        val ownerId = arguments?.getInt(Constants.OWNER_ID_KEY) ?: 0
        autoDispose.add(
            viewModel.reviewOptions(localUserId == ownerId)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    reviewItemsRecycler.adapter = AddReviewItemsAdapter(it.response.reviewOptions)
                    reviewItemsRecycler.layoutManager = LinearLayoutManager(context)
                    reviewItemsRecycler.isNestedScrollingEnabled = true


                    sendBtn.setOnClickListener {
                        if (unitId != 0 && bookingId != 0 && ownerId != 0 && guestId != 0) {
                            val generalReviewStr: String = generalComment.text.trim().toString()
                            if (generalReviewStr.isNotEmpty()) {
                                val userReview = UserReview(
                                    unitId,
                                    ownerId,
                                    guestId,
                                    bookingId,
                                    "review",
                                    generalReviewStr,
                                    arrayListOf()
                                )

                                for (child in reviewItemsRecycler.children) {
                                    val itemRateBar: RatingBar = child.findViewById(R.id.itemRating)
                                    val itemReview: EditText = child.findViewById(R.id.itemReview)
                                    val itemTitle: TextView = child.findViewById(R.id.itemTitle)
                                    Timber.d("${itemTitle.text} -- ${itemReview.text}")
                                    userReview.Items.add(
                                        UserReview.ReviewItem(
                                            itemTitle.text.toString(),
                                            if ( itemRateBar.rating >= 1.0 ) itemRateBar.rating else 1f,
                                            itemReview.text.toString()
                                        )
                                    )


                                }

                                autoDispose.add(
                                    viewModel.addReview(userReview)
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                            displayDialogAndExit()
                                        }, {
                                            Timber.e(it)
                                        })
                                )

                            } else {
                                context?.makeToast("Enter your Review")
                            }
                        }
                    }
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun displayDialogAndExit() {
        context?.let {
            Dialog(it, R.style.FullScreenDialog).apply {
                setContentView(R.layout.dialog_review_added)
                autoDispose.add(
                    Observable.timer(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            dismiss()
                            activity?.onBackPressed()
                        })
            }.show()
        }
    }
}