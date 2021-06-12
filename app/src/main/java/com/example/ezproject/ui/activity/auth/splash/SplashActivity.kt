package com.example.ezproject.ui.activity.auth.splash

import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.ui.activity.LandingActivity
import com.example.ezproject.ui.activity.MainActivity
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.launchActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class SplashActivity : AppCompatActivity() {



    @Inject
    @field:Named("splash")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Fade()
        }


        setContentView(R.layout.activity_splash)

        (application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)

        autoDispose.add(Observable.fromCallable {

        }.subscribeOn(Schedulers.io()).delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                viewModel.userData()?.let {
                    launchActivity(MainActivity::class.java)
                    finish()
                } ?: run {
                    startActivity(
                        Intent(this, LandingActivity::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(
                            this,
                            logoBtn,
                            "logo"
                        ).toBundle()
                    )
                    finish()
                }
            }, {
                Timber.e(it)
            })
        )


        val sharedPreferences: SharedPreferences = getSharedPreferences(
            Constants.Private_or_Hotel,
            0
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("catidd",1)
        editor.apply()
    }
}
