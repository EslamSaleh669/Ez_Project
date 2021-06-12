package com.example.ezproject.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.ezproject.R
import com.example.ezproject.ui.activity.auth.splash.SplashActivity
import com.example.ezproject.util.Constants
import com.example.ezproject.util.extensions.launchActivity
import kotlinx.android.synthetic.main.fragment_change_currency.*
import kotlinx.android.synthetic.main.fragment_settings.backBtn

class ChangeCurrencyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        egpBtn.setOnClickListener {
            context?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.edit {
                putString(Constants.CURRENCY_KEY, "egp")
            }
            activity?.launchActivity(SplashActivity::class.java)
            activity?.finish()
        }

        usdBtn.setOnClickListener {
            context?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.edit {
                putString(Constants.CURRENCY_KEY, "usd")
            }
            activity?.launchActivity(SplashActivity::class.java)
            activity?.finish()

        }
    }
}