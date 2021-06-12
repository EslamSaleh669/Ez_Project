package com.example.ezproject.ui.fragment.unit.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ezproject.R
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_unit_basic.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class BasicInfoFragment : Fragment() {

    /*
    * todo next btn
    *  todo back btn
    *   todo remove toast done
    *
    * */
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
        activity?.findViewById<CardView>(R.id.bottomNav)?.visibility = View.GONE

        return inflater.inflate(R.layout.fragment_add_unit_basic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        arguments?.getInt("id")?.let {
            viewModel.unitId = it
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
            val titleTxt = unitTile.text.trim().toString()
            val descriptionTxt = description.text.trim().toString()

            if (titleTxt.isNotEmpty()) {
                if (descriptionTxt.isNotEmpty()) {

                    viewModel.unitDraft?.unit?.title = titleTxt
                    viewModel.unitDraft?.unit?.description = descriptionTxt

                    autoDispose.add(
                        viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                            {
                                if (it.status == 1) {
                                    context?.makeToast("Done")
                                }
                                activity?.supportFragmentManager?.commit {
                                    replace(R.id.fragmentContainer, UnitOptionsFragment())
                                    addToBackStack("")
                                }
                            },
                            {
                                Timber.e(it)
                                context?.handleApiError(it)
                            })
                    )
                } else {
                    context?.makeToast(getString(R.string.description))
                }
            } else {
                context?.makeToast(getString(R.string.title_required))
            }
        }
    }


    private fun initView() {
        viewModel.unitDraft?.let {
            unitTile.setText(it.unit.title)
            description.setText(it.unit.description)

            guestsCount.setText(it.unit.guests)
            bedsCount.setText(it.unit.beds)
            bathroomsCount.setText(it.unit.bathrooms)
            bedRoomsCount.setText(it.unit.rooms)
            balconsCount.setText(it.unit.balacons)
            unitType.adapter =
                ArrayAdapter(
                    context!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it.options.category.values.toList()
                )


            unitType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    adapter?.selectedItemPosition?.let { position ->
                        viewModel.unitDraft?.unit?.type =
                            it.options.category.keys.elementAt(position)
                    }
                }
            }

            guestsPlusBtn.setOnClickListener {
                var count = guestsCount.text.toString().toInt() + 1
                guestsCount.setText("$count")
                viewModel.unitDraft?.unit?.guests = count.toString()

            }

            guestsMinusBtn.setOnClickListener {
                var count = guestsCount.text.toString().toInt() - 1
                if (count >= 0)
                    guestsCount.setText("$count")
                viewModel.unitDraft?.unit?.guests = count.toString()
            }



            bedsPlusBtn.setOnClickListener {
                var count = bedsCount.text.toString().toInt() + 1
                bedsCount.setText("$count")
                viewModel.unitDraft?.unit?.beds = count.toString()

            }

            bedsMinusBtn.setOnClickListener {
                var count = bedsCount.text.toString().toInt() - 1
                if (count >= 0)
                    bedsCount.setText("$count")
                viewModel.unitDraft?.unit?.beds = count.toString()
            }

            bedRoomsPlusBtn.setOnClickListener {
                var count = bedRoomsCount.text.toString().toInt() + 1
                bedRoomsCount.setText("$count")
                viewModel.unitDraft?.unit?.rooms = count.toString()

            }

            bedRoomsMinusBtn.setOnClickListener {
                var count = bedRoomsCount.text.toString().toInt() - 1
                if (count >= 0)
                    bedsCount.setText("$count")
                viewModel.unitDraft?.unit?.rooms = count.toString()
            }


            bathroomsPlusBtn.setOnClickListener {
                var count = bathroomsCount.text.toString().toInt() + 1
                bathroomsCount.setText("$count")
                viewModel.unitDraft?.unit?.bathrooms = count.toString()

            }

            bathroomsMinusBtn.setOnClickListener {
                var count = bathroomsCount.text.toString().toInt() - 1
                if (count >= 0)
                    bathroomsCount.setText("$count")
                viewModel.unitDraft?.unit?.bathrooms = count.toString()
            }



            balconsPlusBtn.setOnClickListener {
                var count = balconsCount.text.toString().toInt() + 1
                balconsCount.setText("$count")
                viewModel.unitDraft?.unit?.balacons = count.toString()

            }

            balconsMinusBtn.setOnClickListener {
                var count = balconsCount.text.toString().toInt() - 1
                if (count >= 0)
                    balconsCount.setText("$count")
                viewModel.unitDraft?.unit?.balacons = count.toString()
            }

        }
    }

}