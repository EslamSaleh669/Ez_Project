package com.example.ezproject.ui.fragment.profile.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ezproject.R
import com.example.ezproject.ui.fragment.payment.PayInFragment
import com.example.ezproject.ui.fragment.payment.PayoutFragment
import com.example.ezproject.ui.fragment.profile.EditProfileFragment
import com.example.ezproject.ui.fragment.profile.ProfileViewModel
import com.example.ezproject.ui.fragment.profile.booking.UserBookingFragment
import com.example.ezproject.ui.fragment.profile.UserUnitsFragment
import com.example.ezproject.ui.fragment.profile.reviews.UserReviewsFragment
import com.example.ezproject.ui.fragment.unit.add.BasicInfoFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_become_host.*

import javax.inject.Inject
import javax.inject.Named

class BecomeHostFragment : Fragment(), View.OnClickListener {

    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_become_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        addUnitAction.setOnClickListener(this)
        myUnit.setOnClickListener(this)
        myUnitsRequests.setOnClickListener(this)
        editUserData.setOnClickListener(this)
        payInsAction.setOnClickListener(this)
        payoutsAction.setOnClickListener(this)
        myUnitsReviewsAction.setOnClickListener(this)
        viewModel.currentUser()?.let { user ->
            userName.text = user.name
            Glide.with(activity!!).load("${Constants.STORAGE_URL}${user.avatar}")
                .error(R.drawable.ic_account)
                .placeholder(R.drawable.ic_account).apply(
                    RequestOptions.bitmapTransform(CropCircleTransformation())
                ).into(userImage)

        }
    }

    override fun onClick(view: View?) {
        val fragment: Fragment? = when (view?.id) {

            R.id.editUserData -> EditProfileFragment()
            R.id.myUnit -> UserUnitsFragment()
            R.id.myUnitsRequests -> UserBookingFragment()
                .apply {
                    arguments = bundleOf(Pair("p", 1))
                }

            R.id.addUnitAction -> BasicInfoFragment()
            R.id.payInsAction -> PayInFragment()
            R.id.payoutsAction -> PayoutFragment()
            R.id.myUnitsReviewsAction -> UserReviewsFragment().apply {
                arguments = bundleOf(Pair("p", true))
            }
            else -> null
        }

        fragment?.let {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, it)
                addToBackStack("")
            }
        }
    }

}