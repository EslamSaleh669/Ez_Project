package com.example.ezproject.ui.fragment.profile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.ezproject.R
import com.example.ezproject.data.models.User
import com.example.ezproject.ui.fragment.main.MainFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.launchLoadingDialog
import com.example.ezproject.util.extensions.makeToast
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.backBtn
import kotlinx.android.synthetic.main.fragment_edit_profile.userImage
import kotlinx.android.synthetic.main.myalertdialog.view.*
import timber.log.Timber
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


class EditProfileFragment : Fragment() {
    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()

    private var userImageFile: File? = null
    private var userIdFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = context?.launchLoadingDialog()
        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                dialog?.dismiss()
                timer2.cancel() //this will cancel the timer of the system
            }
        }, 3000)





        backBtn.setOnClickListener {
            //activity?.onBackPressed()
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, MainFragment().apply {
                })}
        }
        loadUser()



    }


    private fun loadUser() {
        autoDispose.add(
            viewModel.userOnline().observeOn(AndroidSchedulers.mainThread()).subscribe(
                { initViews(it)

                    Log.d("logged user", it.toString())


                }, {

                    context?.makeToast("no logged user")
                    activity?.onBackPressed()
       //             Log.d("profilerrors",it.message)
                })
        )
    }

    private fun initViews(user: User) {

        verifyPercent.currentProgress = user.verifPercent!!
        percentVerifyTxt.text = "${user.verifPercent }%"


         Glide.with(activity!!).load("${Constants.STORAGE_URL}${user.avatar}")
             .listener(object : RequestListener<Drawable>
             {
                 override fun onLoadFailed(
                     e: GlideException?,
                     model: Any?,
                     target: Target<Drawable>?,
                     isFirstResource: Boolean): Boolean {
//                     activity!!.makeToast("Failed to load image")
                     return false
                 }

                 override fun onResourceReady(
                     resource: Drawable?,
                     model: Any?,
                     target: Target<Drawable>?,
                     dataSource: DataSource?,
                     isFirstResource: Boolean
                 ): Boolean {
                     return true
                 }
             }
             )
            .placeholder(R.drawable.ic_account)
            .error(R.drawable.ic_account)
            .circleCrop().into(userImage)


        userImage.setOnClickListener {
            RxPaparazzo.single(this)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    if (response.resultCode() == RESULT_OK) {
                        Glide.with(activity!!).load(response.data().file)
                            .placeholder(R.drawable.ic_account)
                             .error(R.drawable.ic_account)
                            .circleCrop().into(userImage)
                        userImageFile = response.data().file
                    }
                }
        }
        changePasswordBtn.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, EditPasswordFragment())
                addToBackStack("")
            }
        }
        if (user.emailVerifiedAt == null) {
            verifyEmailLayout.visibility = View.VISIBLE
            emailVerified.visibility = View.GONE


            startVerifyEmail.setOnClickListener {
                autoDispose.add(
                    viewModel.verifyEmail().observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            context?.makeToast("You should have received a verification email")
                            /*if (it.response.result) {
                            } else {
                                context?.makeToast("an error occurred")
                            }*/
                        }, {
                            Timber.e(it)
                        })
                )
            }
        } else {
            verifyEmailLayout.visibility = View.GONE
            emailVerified.visibility = View.VISIBLE

        }

        if (user.mobileVerifiedAt == null) {
            phoneKey.registerCarrierNumberEditText(phoneInput)
            verifyPhoneLayout.visibility = View.VISIBLE
            phoneVerified.visibility = View.GONE
            startVerifyPhone.setOnClickListener {

                if (phoneKey.fullNumberWithPlus.trim().length != 13){

                    context?.makeToast(getString(R.string.enter_valid_phone))

                }else{
                    autoDispose.add(
                        viewModel.verifyPhone(phoneKey.fullNumberWithPlus.trim()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                    ValidatePhoneVerificationCodeDialog(it) {
                                        context?.let {
                                            Dialog(it, R.style.FullScreenDialog).apply {
                                                setContentView(R.layout.dialog_verify_pass_complete)
                                                autoDispose.add(
                                                    Observable.timer(
                                                        5,
                                                        TimeUnit.SECONDS
                                                    ).observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe {
                                                            dismiss()
                                                            loadUser()
                                                        }
                                                )
                                            }.show()
                                        }
                                    }.show(
                                        activity!!.supportFragmentManager,
                                        ""
                                    )


                            }, {
                                Timber.e(it)
                                context?.makeToast(getString(R.string.this_num_has_taken))
                            })
                    )
                }


            }
        } else {
            phoneVerified.visibility = View.VISIBLE
            verifyPhoneLayout.visibility = View.GONE
        }

        if (user.photoidVerifiedAt == null) {
            verifyNatId.visibility = View.VISIBLE
             uploadNatId.setOnClickListener {

                 RxPaparazzo.single(this)
                     .usingGallery()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe { response ->
                         if (response.resultCode() == RESULT_OK) {
                             autoDispose.add(
                                 viewModel.uploadNatIdImg(response.data().file)
                                     .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                         if (it.user.photoid != null) {

                                             val mDialogView = LayoutInflater.from(context)
                                                 .inflate(R.layout.myalertdialog, null)
                                             val mBuilder = AlertDialog.Builder(context)
                                                 .setView(mDialogView)

                                             mDialogView.dialogtitle.text =
                                                 "National ID Uploaded"
                                             mDialogView.dialogmessage.text = "Wait for review"
                                             val mAlertDialog = mBuilder.show()
                                             mDialogView.dilogok.setOnClickListener {
                                                 mAlertDialog.dismiss()
                                             }
                                         }
//                                             val alert = dialogBuilder.create()
//                                             alert.setTitle("National ID Uploaded")
//                                             alert.show()
//


                                     }, {
                                         Timber.e(it)
                                     })
                             )
                         }
                     }

          }
        }
//
        //Log.d("phonesendedrec",user.mobile)
//
        if (user.mobile == null){

        context?.makeToast(getString(R.string.you_must))

        }else{
            phoneKey.registerCarrierNumberEditText(phoneInput)
            if (user.mobile.first() == '2'){
                user.mobile = "+" + user.mobile
            }
            if (user.mobile.substring(0,1) != "+2"){
                user.mobile = "+2" + user.mobile
            }



        }

        userName.text = user.name
        fullNameInput.setText(user.name)
        emailInput.setText(user.email)
        locationInput.setText(user.city)
        aboutMeInput.setText(user.description)

        saveBtn.setOnClickListener {
            val fName = fullNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val location = locationInput.text.toString().trim()
            var phone = phoneKey.fullNumberWithPlus.trim()
            val desc = aboutMeInput.text.toString().trim()
            val pass = passwordInput.text.trim().toString()
            if (fName.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                if (phoneKey.isValidFullNumber) {
                    autoDispose.add(
                        viewModel.updateUser(
                            fName,
                            email,
                            phone,
                            desc,
                            location,
                            userImageFile
                        ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                            if (it.status == Constants.STATUS_OK) {
                                it.user.token = user.token
                                viewModel.saveUserData(it.user)
                                context?.makeToast(getString(R.string.done_user_info))
                                activity?.onBackPressed()
//                            activity?.startActivity(
//                                Intent(
//                                    activity!!,
//                                    MainActivity::class.java
//                                ).apply {
//                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                                    )
//                                })
//                            activity?.finish()
                            } else {
                                context?.makeToast(getString(R.string.error_user_info))
                            }
                        }, {
                            Timber.e(it)
                            context?.handleApiError(it)
                            context?.makeToast(getString(R.string.this_num_has_taken))
                        })
                    )
                } else {
                    context?.makeToast(getString(R.string.enter_valid_phone))
                }
            } else {
                context?.makeToast(getString(R.string.fill_req))
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, MainFragment().apply {
                })}        }
    }





}
//
//                               val options =
//                     arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
//                 val builder =
//                     AlertDialog.Builder(activity!!)
//                 builder.setTitle("Add Photo!");
//                 builder.setItems(options) { dialog, item ->
//                     if (options[item].equals("Take Photo")) {
//
//                         RxPaparazzo.single(this)
//                             .usingCamera()
//                             .subscribeOn(Schedulers.io())
//                             .observeOn(AndroidSchedulers.mainThread())
//                             .subscribe { response ->
//                                 if (response.resultCode() == RESULT_OK) {
//                                     autoDispose.add(
//                                         viewModel.uploadNatIdImg(response.data().file)
//                                             .observeOn(AndroidSchedulers.mainThread()).subscribe({
//                                                 if (it.user.photoid != null) {
//                                                     context?.makeToast("Wait for review")
//                                                 }
//                                             }, {
//                                                 Timber.e(it)
//
//                                             })
//                                     )
//                                 }
//                             }
//
//                     } else if (options[item].equals("Choose from Gallery")) {
//
//
//                         RxPaparazzo.single(this)
//                             .usingGallery()
//                             .subscribeOn(Schedulers.io())
//                             .observeOn(AndroidSchedulers.mainThread())
//                             .subscribe { response ->
//                                 if (response.resultCode() == RESULT_OK) {
//                                     autoDispose.add(
//                                         viewModel.uploadNatIdImg(response.data().file)
//                                             .observeOn(AndroidSchedulers.mainThread()).subscribe({
//                                                 if (it.user.photoid != null) {
//                                                     context?.makeToast("Wait for review")
//                                                 }
//                                             }, {
//                                                 Timber.e(it)
//                                             })
//                                     )
//                                 }
//                             }
//
//
//                     } else if (options[item].equals("Cancel")) {
//                         dialog.dismiss()
//                     }
//                 }
//                 builder.show()