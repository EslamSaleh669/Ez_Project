package com.example.ezproject.ui.fragment.profile.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import kotlinx.android.synthetic.main.cancelation_policy_dialog_layout.*
import kotlinx.android.synthetic.main.dialog_validate_phone_code.*
import kotlinx.android.synthetic.main.dialog_validate_phone_code.backBtn
import javax.inject.Inject
import javax.inject.Named

class Cancelation_Policy_Dialog(
    var data: String,
    var title: String,
    function: () -> Unit?
) : DialogFragment() {

    @Inject
    @field:Named("unitDetails")
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel: UnitDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UnitDetailsViewModel::class.java)
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
        return inflater.inflate(R.layout.cancelation_policy_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mydata.text = data
         backBtn.setOnClickListener {
            dismiss()
        }


     }

 

    override fun onDestroyView() {
        super.onDestroyView()
     }
}