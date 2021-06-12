package com.example.ezproject.ui.fragment.profile

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_reservation_details.*
import kotlinx.android.synthetic.main.walletlayout.*
import kotlinx.android.synthetic.main.walletlayout.backBtn

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class Wallet : Fragment() {

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
        return inflater.inflate(R.layout.walletlayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }


        autoDispose.add(
            viewModel.userOnline().observeOn(AndroidSchedulers.mainThread()).subscribe({ user ->

                if (user.status == 200){
                    userwallet.text = "${user.loyaltyAccountUser!!.walletCurrentBalance?.toString()}${viewModel.currency}"

                }else{
                    activity!!.makeToast(getString(R.string.network_error))
                }


            }, {
                activity?.onBackPressed()
                Timber.e(it)
            })
        )



    }


}