package com.example.ezproject.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import com.example.ezproject.R
import com.example.ezproject.ui.activity.auth.login.LoginActivity
import com.example.ezproject.ui.activity.auth.register.RegisterActivity
import com.example.ezproject.util.extensions.getKeyHash
import kotlinx.android.synthetic.main.activity_landing.*
import timber.log.Timber

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Fade()
        }
        setContentView(R.layout.activity_landing)

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        toSignInBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


//        Timber.d(getKeyHash())
    }
}
