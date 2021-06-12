package com.example.ezproject.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.ezproject.R
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.ui.fragment.chat.chatrooms.ChatRoomsFragment
import com.example.ezproject.ui.fragment.main.MainFragment
import com.example.ezproject.ui.fragment.profile.EditProfileFragment
import com.example.ezproject.ui.fragment.profile.ProfileFragment
import com.example.ezproject.ui.fragment.profile.notification.NotificationFragment
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment
import com.example.ezproject.ui.fragment.unit.favourite.FavouriteFragment
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.util.Constants
import com.example.ezproject.util.extensions.makeToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


/*

todo when back btn clicked activate right item in bottom nav
*/
class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intentt = intent
        //val bundle:Bundle = intentt.extras!!


        val id = intent.getIntExtra("titlekey",0)

            if (id == 1){
                supportFragmentManager.commit {
                    replace(R.id.fragmentContainer, NotificationFragment())
                    addToBackStack("")
                }
            }else if (id == 2){
                supportFragmentManager.commit {
                    replace(R.id.fragmentContainer,ChatRoomsFragment())
                    addToBackStack("")
                }
            }else{
                supportFragmentManager.commit {
                    replace(R.id.fragmentContainer, MainFragment())
                    addToBackStack("")
                }
            }




        val sharedPreferences: SharedPreferences = getSharedPreferences(
            Constants.preferences_fileName,
            0
        )
        val intent = intent
        val action = intent.action
        val data: Uri? = intent.data
//        if (data != null) {
//
//
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            editor.putInt("selid", 494)
//            editor.apply()
//
//            supportFragmentManager.commit {
//                replace(R.id.fragmentContainer, AllUnitsFragment())
//                addToBackStack("")
//            }
//        }else{





        supportFragmentManager.addOnBackStackChangedListener {
                resetBottomNave()
                if (supportFragmentManager.fragments.last() is FavouriteFragment) {
                    navFavBtn.setImageResource(R.drawable.ic_like_active)
                } else if (supportFragmentManager.fragments.last() is ChatRoomsFragment ||
                    supportFragmentManager.fragments.last() is ChatMessagesFragment
                ) {
                    navMsgBtn.setImageResource(R.drawable.ic_chat_active)
                } else if (supportFragmentManager.fragments.last() is ProfileFragment ||
                    supportFragmentManager.fragments.last() is EditProfileFragment) {
                    navMenuBtn.setImageResource(R.drawable.ic_menue_active)
                } else if (supportFragmentManager.fragments.last() is NotificationFragment) {
                    navNotBtn.setImageResource(R.drawable.ic_small_bell2)
                } else if (supportFragmentManager.fragments.last() is MainFragment) {
                    navLogoBtn.setImageResource(R.drawable.ic_nav_logo)
                }

            }

//        }


        homeNotLayout.setOnClickListener(this)
        favNavLayout.setOnClickListener(this)
        homepagelayout.setOnClickListener(this)
        chatNavLayout.setOnClickListener(this)
        menuNavLayout.setOnClickListener(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
       // handleintent()
    }

    fun resetBottomNave() {
        navNotBtn.setImageResource(R.drawable.ic_small_bell)
        navFavBtn.setImageResource(R.drawable.ic_nav_fav)
        navLogoBtn.setImageResource(R.drawable.ic_nav_logo)
        navMsgBtn.setImageResource(R.drawable.ic_chat_in_active)
        navMenuBtn.setImageResource(R.drawable.ic_nav_menu)
    }

    override fun onClick(view: View?) {


                val uri = intent?.data
        if (uri != null) {
            this.let {
                UnitDetailsFragment.instance(1437)
             }

        }
        resetBottomNave()
        view?.let {
            var fragment: Fragment = when (it.id) {
                R.id.homeNotLayout -> {
                    navNotBtn.setImageResource(R.drawable.ic_small_bell2)
                    NotificationFragment()
                }
                R.id.chatNavLayout -> {
                    navMsgBtn.setImageResource(R.drawable.ic_chat_active)
                    ChatRoomsFragment()
                }
                R.id.homepagelayout -> {
                    navLogoBtn.setImageResource(R.drawable.ic_nav_logo)
                    MainFragment()
                }
                R.id.menuNavLayout -> {
                    navMenuBtn.setImageResource(R.drawable.ic_menue_active)
                    ProfileFragment()
                }
                R.id.favNavLayout -> {
                    navFavBtn.setImageResource(R.drawable.ic_like_active)
                    FavouriteFragment()
                }
                else -> {
                    navLogoBtn.setImageResource(R.drawable.ic_nav_logo)
                    MainFragment()
                }
            }

            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, fragment)
                addToBackStack("")
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.last() is MainFragment ||
           supportFragmentManager.fragments.last() is SupportRequestManagerFragment

        ) {

            val builder =
                AlertDialog.Builder(this, R.style.AlertDialogStyle)
            builder.setCancelable(false)
            builder.setMessage(R.string.insurance_msg)
            builder.setPositiveButton(
                getString(R.string.yes)
            ) { dialog, which -> //if user pressed "yes", then he is allowed to exit from application
                finish()
                moveTaskToBack(true);
                exitProcess(-1)
            }
            builder.setNegativeButton(
                getString(R.string.no)
            ) { dialog, which -> //if user select "No", just cancel this dialog and continue with app
                dialog.cancel()
            }

            val alert = builder.create()
            alert.show()

        } else if (supportFragmentManager.fragments.last() is ChatRoomsFragment ||
            supportFragmentManager.fragments.last() is NotificationFragment) {

            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, MainFragment())
            }
        }else{
            super.onBackPressed()
        }
    }
    companion object {

        private const val TAG = "MainActivity token."
        //efXiAfS5SJWdX9B0NzvJXH:APA91bFehegEKeS3NFenrM4jA9ov65KmQdFTiI9g0yicFHvtR07_nEN6PQEundzuFrtmKYVMWBJAICz9UxoxSoJ2MLJHcwPlt1eiRRs6eOtgL4pNzkms1CGiV72jvzxcmOgkZRmTY80T
    }
 }
