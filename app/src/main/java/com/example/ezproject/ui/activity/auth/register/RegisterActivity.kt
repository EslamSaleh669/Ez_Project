package com.example.ezproject.ui.activity.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.ui.activity.MainActivity
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.launchActivity
import com.example.ezproject.util.extensions.launchActivityFinishCurrent
import com.example.ezproject.util.extensions.makeToast
import com.example.ezproject.util.extensions.makeToastList
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_register.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class RegisterActivity : AppCompatActivity() {

    var msgs : String = ""

    @Inject
    @field:Named("register")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        (application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)

        backBtn.setOnClickListener {
            onBackPressed()
        }


        Firebase.messaging.getToken().addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("LoginActivityFCMerror", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            msgs =  token.toString()
            Log.d("LoginActivityFCM", msgs)
        })


        phoneKey.registerCarrierNumberEditText(phoneInput)
        registerBtn.setOnClickListener {
            if(phoneKey.isValidFullNumber){
            val nameStr = nameInput.text.toString().trim()
            val emailStr = emailInput.text.toString().trim()
            val passwordStr = passwordInput.text.toString().trim()
            val phoneStr = phoneInput.text.toString().trim()
             autoDispose.add(
                viewModel.userRegister(
                    nameStr,
                    emailStr,
                    passwordStr,
                    phoneStr,
                    msgs
                ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    it.user?.let {
                        viewModel.saveUserData(it)
                        launchActivityFinishCurrent(MainActivity::class.java)
                    } ?: makeToastList(it.errors)
                }, {
                    Timber.e(it)
                })
            )
        }else{
                makeToast("Enter valid phone number")
            }
        }
    }
}
