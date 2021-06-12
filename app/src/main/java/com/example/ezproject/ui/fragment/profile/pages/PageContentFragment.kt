package com.example.ezproject.ui.fragment.profile.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ezproject.R
import kotlinx.android.synthetic.main.fragment_static_page_content.*
import timber.log.Timber

class PageContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_static_page_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        arguments?.getString("page")?.let {
            val url = "https://ezuru.com/$it?webview=1"
            Timber.d(url)
            content.loadUrl(url)
            content.settings.javaScriptEnabled = true
        }
    }
}