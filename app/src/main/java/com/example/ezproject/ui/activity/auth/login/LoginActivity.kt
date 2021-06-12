package com.example.ezproject.ui.activity.auth.login
//ga0RGNYHvNM5d0SLGQfpQWAPGJ8=
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.ui.activity.MainActivity
import com.example.ezproject.ui.activity.auth.forget.ForgetPasswrodActivity
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.launchActivity
import com.example.ezproject.util.extensions.launchActivityFinishCurrent
import com.example.ezproject.util.extensions.makeToast
import com.example.ezproject.util.extensions.makeToastList
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.fragment_reservation_done.*


class LoginActivity : AppCompatActivity() {

    @Inject
    @field:Named("login")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()

    lateinit var callbackManager: CallbackManager
    var msgs : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        callbackManager = CallbackManager.Factory.create()
        forgetPassword.setOnClickListener {
            launchActivity(ForgetPasswrodActivity::class.java)
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

        loginBtn.setOnClickListener {


            val emailStr = emailInput.text.toString().trim()
            val passwordStr = passwordInput.text.toString().trim()

            if (emailStr.isEmpty()){
                makeToast("Email field is required")
            }else if (passwordStr.isEmpty()){
                makeToast("Password field is required")
            }else{
                autoDispose.add(
                    viewModel.userLogin(
                        emailStr,
                        passwordStr,
                        msgs

                    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        it.user?.let {
                            viewModel.saveUserData(it)
                            launchActivityFinishCurrent(MainActivity::class.java)
                            Log.d("ttttoken", it.token)
                            Log.d("LoginActivitysuccess", msgs)

                        } ?: makeToastList(it.errors)
                    }, {
                        Timber.e(it)
                        Log.d("loginresponse",it.message)

                        makeToast(getString(R.string.network_error))
                    })
                )
            }
        }

        fbLogin.setOnClickListener {
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.accessToken?.token?.let {
                            autoDispose.add(
                                viewModel.socialSignIn(
                                    it,
                                    "facebook"
                                ).observeOn(AndroidSchedulers.mainThread()).subscribe({

                                    if (it.message.equals("logged in successfully!")){
                                        it.user?.let {
                                            viewModel.saveUserData(it)
                                            launchActivity(MainActivity::class.java)
                                            makeToast(getString(R.string.Login_succes))
                                        } ?: makeToastList(it.errors)
                                    }else if (it.message.equals("Verify email has been sent")){
                                        makeToast(it.message)

                                    }else{
                                        makeToast(it.message)
                                    }

                                }, {
                                    Timber.e(it)
                                    makeToast(getString(R.string.network_error))
                                })
                            )
                        }
                        Timber.d(result?.accessToken?.token)
                        Log.d("ReslultTag",result?.accessToken?.token)
                    }

                    override fun onCancel() {
                        makeToast(getString(R.string.cancelled))
                    }

                    override fun onError(error: FacebookException?) {
                        makeToast(getString(R.string.error))
                        Log.d("errrorrr",error.toString())

                        Timber.e(error)
                    }
                })

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }
        googleLogin.setOnClickListener {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken("56362444672-rttupq12q9qipbl2mjgp1g7rkg2mcsje.apps.googleusercontent.com")
                    .requestServerAuthCode("56362444672-rttupq12q9qipbl2mjgp1g7rkg2mcsje.apps.googleusercontent.com")
                    .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1001)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                Timber.d(account?.serverAuthCode)

                account?.serverAuthCode?.let {


                    val client = OkHttpClient()
                    val requestBody = FormBody.Builder()
                        .add("grant_type", "authorization_code")
                        .add(
                            "client_id",
                            "56362444672-rttupq12q9qipbl2mjgp1g7rkg2mcsje.apps.googleusercontent.com"
                        )
                        .add("client_secret", "Sg0FSIGENxULqTJKhG00QY9s")
                        .add("redirect_uri", "")
                        .add("code", it)
                        .build()

                    client.newCall(
                        Request.Builder()
                            .url("https://oauth2.googleapis.com/token")
                            .post(requestBody)
                            .build()
                    ).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Timber.e(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            JSONObject(response.body?.string()).apply {
                                val token = (this.get("access_token") as String)
                                Log.d("resultss",token)
                                autoDispose.add(
                                    viewModel.socialSignIn(
                                        token,
                                        "google"
                                    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                        it.user?.let {
                                            viewModel.saveUserData(it)
                                            launchActivity(MainActivity::class.java)
                                        } ?: makeToastList(it.errors)
                                    }, {
                                        Timber.e(it)
                                        Log.d("googlerrror",it.message)
                                        makeToast(getString(R.string.network_error))
                                    })
                                )
                            }
                        }
                    })
                }


            } catch (e: ApiException) {
                Timber.e(e)
            }

        }
    }

}
