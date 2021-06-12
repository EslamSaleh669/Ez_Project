package com.example.ezproject.ui.fragment.unit.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_legal_paper.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class AddUnitLegalPaperFragment : Fragment() {

    @Inject
    @field:Named("unitDraft")
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private val viewModel: AddUnitViewModel by lazy {
        ViewModelProvider(activity!!, viewModeFactory).get(AddUnitViewModel::class.java)
    }

    private val autoDispose = AutoDispose()


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
        return inflater.inflate(R.layout.fragment_add_unit_legal_paper, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.unitDraft?.let {
            initView()
        } ?: autoDispose.add(
            viewModel.loadUnitDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    viewModel.unitDraft = it
                    initView()
                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )

        nextBtn.setOnClickListener {
                autoDispose.add(
                    viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            if (it.status == 1) {
                                context?.makeToast("Done")
                            }
                            activity?.supportFragmentManager?.commit {
                                replace(R.id.fragmentContainer, UnitRulesFragment())
                                addToBackStack("")
                            }
                        },
                        {
                            Timber.e(it)
                            context?.handleApiError(it)
                        })
                )


        }


    }


    private fun initView() {
        viewModel.unitDraft?.unit?.contractImage?.let {
            fileUploaded.visibility = View.VISIBLE
            fileUploaded.text = it.split("/").last()
        }
        uploadFile.setOnClickListener {
            autoDispose.add(RxPaparazzo.single(this).usingFiles().subscribe {
                autoDispose.add(
                    viewModel.uploadFile(it.data().file).observeOn(
                        AndroidSchedulers.mainThread()
                    ).subscribe {
                        viewModel.unitDraft?.unit?.contractImage = it.url
                        fileUploaded.visibility = View.VISIBLE
                    })
            })
        }
    }

}