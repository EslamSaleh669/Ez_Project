package com.example.ezproject.ui.fragment.unit.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.collapse
import com.example.ezproject.util.extensions.expand
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_rules.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UnitRulesFragment : Fragment() {


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
        return inflater.inflate(R.layout.fragment_add_unit_rules, container, false)
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
            viewModel.unitDraft?.unit?.maxChildrens = childrenCount.text.toString()
            viewModel.unitDraft?.unit?.maxExtra = maxCount.text.toString()
            viewModel.unitDraft?.unit?.maxDays = maxDaysInput.text.toString()
            viewModel.unitDraft?.unit?.minDays = minDaysInput.text.toString()
            autoDispose.add(
                viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.status == 1) {
                            context?.makeToast("Done")
                        }
                        activity?.supportFragmentManager?.commit {
                            replace(R.id.fragmentContainer, UnitAvailabilityFragment())
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

            minDaysInput.setText(it.unit.minDays)
            maxDaysInput.setText(it.unit.maxDays)
            it.options.cpolicy
            val cpolicyList:ArrayList<String> = ArrayList()
            for (item in it.options.cpolicy.values){
                cpolicyList.add(item[0])
            }
            cancellationPolicySpinner.adapter = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,cpolicyList)
            cancellationPolicySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.unitDraft?.unit?.canclePolicy = it.options.cpolicy.keys.elementAt(p2)
                }
            }
            minGuestCount.setText(viewModel.unitDraft?.unit?.minGuests)
            childrenCount.setText(viewModel.unitDraft?.unit?.maxChildrens)
            maxCount.setText(viewModel.unitDraft?.unit?.maxExtra)

            if (viewModel.unitDraft?.unit?.allowChildrens != "1") {
                allowChild.setImageResource(R.drawable.ic_option_off)
                childCountLayout.collapse()
            } else {
                allowChild.setImageResource(R.drawable.ic_option_on)

                childCountLayout.expand()
            }

            if (viewModel.unitDraft?.unit?.allowAnimals != "1") {
                allowAnimals.setImageResource(R.drawable.ic_option_off)


            } else {
                allowAnimals.setImageResource(R.drawable.ic_option_on)

            }


            if (viewModel.unitDraft?.unit?.allowInfants != "1") {
                allowInfants.setImageResource(R.drawable.ic_option_off)


            } else {
                allowInfants.setImageResource(R.drawable.ic_option_on)

            }

            if (viewModel.unitDraft?.unit?.allowExtra != "1") {
                allowExtra.setImageResource(R.drawable.ic_option_off)
                maxCountLayout.collapse()
            } else {
                allowExtra.setImageResource(R.drawable.ic_option_on)
                maxCountLayout.expand()
            }

            allowChild.setOnClickListener {
                if (viewModel.unitDraft?.unit?.allowChildrens == "1") {
                    viewModel.unitDraft?.unit?.allowChildrens = "0"
                    allowChild.setImageResource(R.drawable.ic_option_off)
                    childCountLayout.collapse()
                } else {
                    viewModel.unitDraft?.unit?.allowChildrens = "1"
                    allowChild.setImageResource(R.drawable.ic_option_on)
                    childCountLayout.expand()
                }

            }
            allowAnimals.setOnClickListener {
                if (viewModel.unitDraft?.unit?.allowAnimals == "1") {
                    viewModel.unitDraft?.unit?.allowAnimals = "0"
                    allowAnimals.setImageResource(R.drawable.ic_option_off)


                } else {
                    viewModel.unitDraft?.unit?.allowAnimals = "1"
                    allowAnimals.setImageResource(R.drawable.ic_option_on)

                }
            }
            allowInfants.setOnClickListener {
                if (viewModel.unitDraft?.unit?.allowInfants == "1") {
                    viewModel.unitDraft?.unit?.allowInfants = "0"
                    allowInfants.setImageResource(R.drawable.ic_option_off)


                } else {
                    viewModel.unitDraft?.unit?.allowInfants = "1"
                    allowInfants.setImageResource(R.drawable.ic_option_on)

                }

            }
            allowExtra.setOnClickListener {
                if (viewModel.unitDraft?.unit?.allowExtra == "1") {
                    viewModel.unitDraft?.unit?.allowExtra = "0"
                    allowExtra.setImageResource(R.drawable.ic_option_off)
                    maxCountLayout.collapse()
                } else {
                    viewModel.unitDraft?.unit?.allowExtra = "1"
                    allowExtra.setImageResource(R.drawable.ic_option_on)
                    maxCountLayout.expand()
                }

            }
        }
    }

}