package com.example.ezproject.ui.fragment.unit.reservation

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import com.example.ezproject.R
import com.example.ezproject.ui.activity.MainActivity
import com.example.ezproject.util.AutoDispose
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_reservation_payment.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ReservationPaymentFragment(val url: String) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)

    }

    val autoDispose = AutoDispose()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservation_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoDispose.bindTo(this.lifecycle)
        reservationContainer.settings.javaScriptEnabled = true
        reservationContainer.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Timber.d(url)
                url?.let {
                    if (it == "https://ezuru.com/") {

                        context?.let {
                            Dialog(it, R.style.FullScreenDialog).apply {
                                setContentView(R.layout.fragment_reservation_done)
                                show()
                                autoDispose.add(Observable.fromCallable {

                                }.delay(5, TimeUnit.SECONDS).subscribe {
                                    activity?.startActivity(
                                        Intent(
                                            activity!!,
                                            MainActivity::class.java
                                        ).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        })
                                    activity?.finish()
                                    dismiss()
                                })
                            }

                        }
                    }
                }
                return false
            }
        }
        reservationContainer.loadUrl(url)

    }
}