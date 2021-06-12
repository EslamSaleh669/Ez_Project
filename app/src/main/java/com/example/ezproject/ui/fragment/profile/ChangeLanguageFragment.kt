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
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.fragment_change_language.*
import java.util.*


class ChangeLanguageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        arBtn.setOnClickListener {
            context?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.edit {
                putString(Constants.LANG_KEY, "ar")
            }

            Lingver.getInstance().setLocale(context!!, Locale("ar"))
                activity?.launchActivity(SplashActivity::class.java)
                activity?.finish()
        }

        enBtn.setOnClickListener {
            context?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.edit {
                putString(Constants.LANG_KEY, "en")
            }
            Lingver.getInstance().setLocale(context!!, Locale("en"))
            activity?.launchActivity(SplashActivity::class.java)
            activity?.finish()
        }
    }
}
