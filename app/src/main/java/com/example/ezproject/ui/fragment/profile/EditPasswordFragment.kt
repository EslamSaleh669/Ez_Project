package com.example.ezproject.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_update_password.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class EditPasswordFragment : Fragment() {
    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()

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
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePasswordBtn.setOnClickListener {
            val newPassword = newPasswordInput.text.toString()
            val newPasswordCon = newPasswordConInput.text.toString()
            if (newPassword.isNotEmpty() && newPasswordCon.isNotEmpty() && newPassword == newPasswordCon) {
                autoDispose.add(
                    viewModel.updateUserPassword(newPassword).observeOn(
                        AndroidSchedulers.mainThread()
                    ).subscribe({
                        if (it.status == Constants.STATUS_OK) {
                            context?.makeToast(getString(R.string.done_updating_pass))
                            activity?.onBackPressed()
                        }
                    }, {
                        Timber.e(it)
                        context?.handleApiError(it)
                    })
                )
            } else {
                context?.makeToast(getString(R.string.the_same_pass))
            }
        }


    }
}