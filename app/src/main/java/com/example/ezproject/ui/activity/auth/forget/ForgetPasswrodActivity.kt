package com.example.ezproject.ui.activity.auth.forget

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.ui.activity.LandingActivity
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.launchActivity
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_forget_passwrod.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ForgetPasswrodActivity : AppCompatActivity() {


    @Inject
    @field:Named("forget")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ForgetPasswordViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ForgetPasswordViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_passwrod)
        (application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        nextBtn.setOnClickListener {
            val emaiStr = emailInput.text.toString().trim()
            if (emaiStr.length >= 5) {
                autoDispose.add(
                    viewModel.forgetPassword(emaiStr).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            if (it.status == Constants.STATUS_OK) {
                                makeToast(it.message)
                                val dialog = Dialog(this, R.style.FullScreenDialog)
                                dialog.setContentView(R.layout.dialog_check_mail)
                                dialog.findViewById<ImageView>(R.id.backBtn).setOnClickListener {
                                    dialog.dismiss()
                                    launchActivity(LandingActivity::class.java)
                                }
                                dialog.show()
                            }
                        },
                        {
                            Timber.e(it)
                        })
                )
            } else {
                makeToast(getString(R.string.valid_email))
            }

        }
    }
}
