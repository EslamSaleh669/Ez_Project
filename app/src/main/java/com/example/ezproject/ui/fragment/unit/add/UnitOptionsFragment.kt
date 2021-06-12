package com.example.ezproject.ui.fragment.unit.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_options.*
import kotlinx.android.synthetic.main.fragment_add_unit_options.backBtn
import kotlinx.android.synthetic.main.fragment_add_unit_options.nextBtn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UnitOptionsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_add_unit_options, container, false)
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
                            replace(R.id.fragmentContainer, PickUnitLocationFragment())
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
        viewModel.unitDraft?.let {
            for (ame in it.options.aminites) {
                val optionView = LayoutInflater.from(context)
                    .inflate(R.layout.filter_option_item_layout, ameOptionsLayout, false)
                optionView.findViewById<CheckBox>(R.id.optionCheck)

                optionView.setOnClickListener {
                    optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                    viewModel.unitDraft?.unit?.options?.aminites?.let {
                        if (it.contains(ame.key)) {
                            viewModel.unitDraft?.unit?.options?.aminites?.remove(ame.key)

                        } else {
                            viewModel.unitDraft?.unit?.options?.aminites?.add(ame.key)
                        }
                    }
                }
                optionView.findViewById<TextView>(R.id.optionTitle).text = ame.value
                viewModel.unitDraft?.unit?.options?.aminites?.let {
                    if (it.contains(ame.key)) {
                        optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                    }
                }
                ameOptionsLayout.addView(optionView)
            }

            for (restItem in it.options.rest) {
                val optionView = LayoutInflater.from(context)
                    .inflate(R.layout.filter_option_item_layout, facOptionsLayout, false)
                optionView.findViewById<CheckBox>(R.id.optionCheck)

                optionView.setOnClickListener {
                    optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                    viewModel.unitDraft?.unit?.options?.rest?.let {
                        if (it.contains(restItem.key)) {
                            viewModel.unitDraft?.unit?.options?.rest?.remove(restItem.key)

                        } else {
                            viewModel.unitDraft?.unit?.options?.rest?.add(restItem.key)

                        }
                    }

                }
                optionView.findViewById<TextView>(R.id.optionTitle).text = restItem.value
                viewModel.unitDraft?.unit?.options?.rest?.let {
                    if (it.contains(restItem.key)) {
                        optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                    }
                }
                facOptionsLayout.addView(optionView)
            }

            for (viewItem in it.options.views) {
                val optionView = LayoutInflater.from(context)
                    .inflate(R.layout.filter_option_item_layout, viewsOptionsLayout, false)
                optionView.findViewById<CheckBox>(R.id.optionCheck)

                optionView.setOnClickListener {
                    optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                    viewModel.unitDraft?.unit?.options?.views?.let {
                        if (it.contains(viewItem.key)) {
                            viewModel.unitDraft?.unit?.options?.views?.remove(viewItem.key)

                        } else {
                            viewModel.unitDraft?.unit?.options?.views?.add(viewItem.key)

                        }
                    }

                }
                optionView.findViewById<TextView>(R.id.optionTitle).text = viewItem.value
                viewModel.unitDraft?.unit?.options?.views?.let {
                    if (it.contains(viewItem.key)) {
                        optionView.findViewById<CheckBox>(R.id.optionCheck).isChecked = true
                    }
                }
                viewsOptionsLayout.addView(optionView)
            }

        }
    }
}