package com.example.ezproject.ui.fragment.profile.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.ezproject.R
import kotlinx.android.synthetic.main.fragment_static_pages.*

class PagesFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_static_pages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        backBtn.setOnClickListener{
            activity?.onBackPressed()
        }
        aboutAction.setOnClickListener(this)
        whyAction.setOnClickListener(this)
        newsAction.setOnClickListener(this)
        checkInAction.setOnClickListener(this)
        contactInAction.setOnClickListener(this)
        faqInAction.setOnClickListener(this)
        howWrokInAction.setOnClickListener(this)
        partinersAction.setOnClickListener(this)
        paymentMethodsAction.setOnClickListener(this)
        trustSafteyAction.setOnClickListener(this)
        cancleAction.setOnClickListener(this)
        blogAction.setOnClickListener(this)
        careersAction.setOnClickListener(this)
        termsAction.setOnClickListener(this)
        privacyAction.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        activity?.supportFragmentManager?.commit {
            replace(R.id.fragmentContainer, PageContentFragment().apply {
                arguments = bundleOf(Pair("page", view?.tag as String)
                )
            })
            addToBackStack("")
        }
    }
}