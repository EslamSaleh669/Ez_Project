package com.example.ezproject.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_validate_phone_code.*
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class ValidatePhoneVerificationCodeDialog(var code: String,val onComplete:()->Unit) : DialogFragment() {

    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val timerDispose = CompositeDisposable()

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_validate_phone_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTimer()
        backBtn.setOnClickListener {
            dismiss()
        }
//        resendSMS.setOnClickListener {
//            autoDispose.add(
//                viewModel.verifyPhone().observeOn(AndroidSchedulers.mainThread()).subscribe({
//                    code = it
//                    initTimer()
//                }, {
//                    Timber.e(it)
//
//                })
//            )
//        }

        confirmBtn.setOnClickListener {
            val codeTxt = codeInput.text.toString().trim()
            if (codeTxt.isNotEmpty()) {
                if (codeTxt == code) {
                    autoDispose.add(
                        viewModel.validatePhone(code).observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                context?.makeToast("Done, verifying your phone")
                                dismiss()
                                onComplete()
                            }, {
                                Timber.e(it)
                            })
                    )
                } else {
                    wrongTxt.visibility = View.VISIBLE
                }
            } else {
                context?.makeToast("Enter code in received SMS")
            }

        }
    }

    private fun initTimer() {
        val maxTime = (5 * 60) - 1
        timerTxt.visibility = View.VISIBLE
        resendSMS.visibility = View.GONE

        timerDispose.add(
            Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val currentTime = maxTime - it
                    if (currentTime > 0) {
                        timerTxt.text = "${(currentTime / 60)}:${(currentTime % 60)}"
                    } else {
                        timerDispose.clear()
                        timerTxt.visibility = View.GONE
                        resendSMS.visibility = View.VISIBLE
                    }
                })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        timerDispose.clear()
    }
}