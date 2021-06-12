package com.example.ezproject.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.ezproject.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        privacyPolicyBtn.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, InformativeFragment())
                addToBackStack("")
            }
        }

        currencyBtn.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, ChangeCurrencyFragment())
                addToBackStack("")
            }
        }
    }
}