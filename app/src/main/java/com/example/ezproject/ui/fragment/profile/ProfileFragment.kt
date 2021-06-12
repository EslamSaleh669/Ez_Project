package com.example.ezproject.ui.fragment.profile

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.ezproject.R
import com.example.ezproject.ui.activity.LandingActivity
import com.example.ezproject.ui.fragment.payment.PayInFragment
import com.example.ezproject.ui.fragment.payment.PayoutFragment
import com.example.ezproject.ui.fragment.profile.booking.UserBookingFragment
import com.example.ezproject.ui.fragment.profile.host.BecomeHostFragment
import com.example.ezproject.ui.fragment.profile.Wallet
import com.example.ezproject.ui.fragment.profile.notification.NotificationFragment
import com.example.ezproject.ui.fragment.profile.pages.PagesFragment
import com.example.ezproject.ui.fragment.profile.payment.PaymentInfoFragment
import com.example.ezproject.ui.fragment.profile.reviews.UserReviewsFragment
import com.example.ezproject.ui.fragment.unit.add.BasicInfoFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.launchActivity
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.userImage
import kotlinx.android.synthetic.main.fragment_profile.userName
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ProfileFragment : Fragment(), View.OnClickListener {
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
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }



        if (viewModel.currentUser()!!.loyaltyAccountUser == null){
                walletbtn.visibility = View.GONE
        }

        autoDispose.add(
            viewModel.userOnline().observeOn(AndroidSchedulers.mainThread()).subscribe({ user ->
                userName.text = user.name

                Glide.with(activity!!).load("${Constants.STORAGE_URL}${user.avatar}")
                    .listener(object :RequestListener<Drawable>
                    {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return true
                        }
                    }
                    )
                    .error(R.drawable.ic_account)
                    .placeholder(R.drawable.ic_account).apply(
                        RequestOptions.bitmapTransform(CropCircleTransformation())
                    ).into(userImage)

                currencyTxt.text = viewModel.currency.toUpperCase()
                languageTxt.text = viewModel.language.toUpperCase()
//                notificationAction.setOnClickListener(this)
                inviteUserAction.setOnClickListener {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Hey! my application now available on PlayStore.\n \n ${Constants.APP_PLAY_STORE_URL}")
                         type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)

                }
                feedbackAction.setOnClickListener(this)
                paymentAction.setOnClickListener(this)
                editUserData.setOnClickListener(this)
                myUnit.setOnClickListener(this)
                myUnitsRequests.setOnClickListener(this)
                myBookings.setOnClickListener(this)
                currencyBtn.setOnClickListener(this)
                languageBtn.setOnClickListener(this)
                addUnitAction.setOnClickListener(this)
                payInsAction.setOnClickListener(this)
                payoutsAction.setOnClickListener(this)
                becomeHost.setOnClickListener(this)
                staticPagesBtn.setOnClickListener(this)
                myReviewsAction.setOnClickListener(this)
                walletbtn.setOnClickListener(this)
            }, {
                 activity?.onBackPressed()
                Timber.e(it)
             })
        )
        logoutBtn.setOnClickListener {
            viewModel.logout()
            activity?.launchActivity(LandingActivity::class.java)
            activity?.finish()
        }
    }

    override fun onClick(view: View?) {
        val fragment: Fragment? = when (view?.id) {
//            R.id.notificationAction -> NotificationFragment()
//            R.id.inviteUserAction -> InviteFriendsFragment()
            R.id.feedbackAction -> FeedbackFragment()
            R.id.paymentAction -> PaymentInfoFragment()
            R.id.editUserData -> EditProfileFragment()
            R.id.becomeHost -> BecomeHostFragment()
            R.id.myUnit -> UserUnitsFragment()
            R.id.myUnitsRequests -> UserBookingFragment()
                .apply {
                    arguments = bundleOf(Pair("p", 1))
                }
            R.id.myBookings -> UserBookingFragment()
            R.id.currencyBtn -> ChangeCurrencyFragment()
            R.id.languageBtn -> ChangeLanguageFragment()
            R.id.addUnitAction -> BasicInfoFragment()
            R.id.payInsAction -> PayInFragment()
            R.id.payoutsAction -> PayoutFragment()
            R.id.staticPagesBtn -> PagesFragment()
            R.id.myReviewsAction -> UserReviewsFragment()
            R.id.walletbtn -> Wallet()

            else -> null
        }

        fragment?.let {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, it)
                addToBackStack("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.VISIBLE

    }
}