package com.example.ezproject.ui.fragment.unit.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.network.response.ImageUploadResponse
import com.example.ezproject.data.network.response.UnitDraftResponse
import com.example.ezproject.ui.adapter.AddUnitImageAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.experiance_item_layout.*
import kotlinx.android.synthetic.main.fragment_add_unit_images.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class PickUnitImagesFragment : Fragment(), AddUnitImageAdapter.OnAddClickListener {


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
        return inflater.inflate(R.layout.fragment_add_unit_images, container, false)
    }

    /*
    * description required
    * min 5 images
    * */

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
            val imageNum = viewModel.unitDraft?.unit?.attachments?.size ?: 0
            if (imageNum >= 5) {
                    autoDispose.add(
                        viewModel.saveDraft().observeOn(AndroidSchedulers.mainThread()).subscribe(
                            {
                                if (it.status == 1) {
                                    context?.makeToast("Done")
                                }
                                activity?.supportFragmentManager?.commit {
                                    replace(R.id.fragmentContainer, AddUnitLegalPaperFragment())
                                    addToBackStack("")
                                }
                            },
                            {
                                Timber.e(it)
                                context?.handleApiError(it)


                            })
                    )

            } else {
                context?.makeToast(getString(R.string.image_required))

            }
        }
    }
     private fun initView() {
        viewModel.unitDraft?.let {
            it.unit.contractImage?.let {
                contractUploaded.visibility = View.GONE
            }
            if (it.unit.attachments.isNotEmpty()) {
                addImages.visibility = View.GONE
                imageUploadedLayout.visibility = View.VISIBLE
                imagesRecycler.adapter = AddUnitImageAdapter(arrayListOf(), activity, this, {
                    viewModel.unitDraft?.unit?.attachments?.removeAt(it)
                }, { position ->
                    RxPaparazzo.single(this).usingGallery().subscribe ({
                        autoDispose.add(
                            viewModel.uploadImage(it.data().file).observeOn(
                                AndroidSchedulers.mainThread()
                            ).subscribe({
                                viewModel.unitDraft?.unit?.attachments?.get(position)?.image =
                                    it.url
                                (imagesRecycler.adapter as AddUnitImageAdapter).updateImage(
                                    position,
                                    it
                                )
                            }, {
                                Timber.e(it)
                            })
                        )
                    },{
                        Timber.e(it)
                        Log.d("myerrors",it.message)

                    })
                }
                )
                imagesRecycler.layoutManager = GridLayoutManager(context, 3)
                for (image in it.unit.attachments) {
                    (imagesRecycler.adapter as AddUnitImageAdapter).addImage(
                        ImageUploadResponse(
                            "",
                            true,
                            image.image!!
                        )
                    )
                }
            }

            addImages.setOnClickListener {
                RxPaparazzo.multiple(this).usingGallery().subscribe ({
                    if (it.data().isNotEmpty()) {
                        addImages.visibility = View.GONE
                        imageUploadedLayout.visibility = View.VISIBLE
                        imagesRecycler.adapter =
                            AddUnitImageAdapter(arrayListOf(), activity, this, {
                                viewModel.unitDraft?.unit?.attachments?.removeAt(it)
                            }, { position ->
                                RxPaparazzo.single(this).usingGallery().subscribe {
                                    autoDispose.add(
                                        viewModel.uploadImage(it.data().file).observeOn(
                                            AndroidSchedulers.mainThread()
                                        ).subscribe({
                                            viewModel.unitDraft?.unit?.attachments?.get(position)
                                                ?.image = it.url
                                            (imagesRecycler.adapter as AddUnitImageAdapter).updateImage(
                                                position,
                                                it
                                            )
                                        }, {
                                            error("")
                                            Timber.e(it)
                                        })
                                    )
                                }
                            })
                        imagesRecycler.layoutManager = GridLayoutManager(context, 3)
                        for (image in it.data()) {
                            autoDispose.add(
                                viewModel.uploadImage(image.file).observeOn(
                                    AndroidSchedulers.mainThread()
                                ).subscribe({
                                    viewModel.unitDraft?.unit?.attachments?.add(
                                        UnitDraftResponse.Attachment(
                                            null,
                                            null,
                                            it.url,
                                            0,
                                            null,
                                            "null",
                                            null,
                                            null
                                        )
                                    )
                                    (imagesRecycler.adapter as AddUnitImageAdapter).addImage(it)
                                }, {
                                    Timber.e(it)
                                    context?.handleApiError(it)
                                })
                            )
                        }
                    }

                },{
                    Log.d("error","error")
                })
            }
            uploadContractImage.setOnClickListener {
                RxPaparazzo.single(this).usingFiles().subscribe {
                    autoDispose.add(
                        viewModel.uploadImage(it.data().file).observeOn(
                            AndroidSchedulers.mainThread()
                        ).subscribe {
                            viewModel.unitDraft?.unit?.contractImage = it.url
                            contractUploaded.visibility = View.VISIBLE
                        })
                }
            }
        }
    }

    override fun onAddClick() {



        RxPaparazzo.multiple(this).usingGallery().subscribe ({
            if (it.data().isNotEmpty()) {
                for (image in it.data()) {
                    autoDispose.add(
                        viewModel.uploadImage(image.file).observeOn(
                            AndroidSchedulers.mainThread()
                        ).subscribe({
                            viewModel.unitDraft?.unit?.attachments?.add(
                                UnitDraftResponse.Attachment(
                                    null,
                                    null,
                                    it.url,
                                    0,
                                    null,
                                    "null",
                                    null,
                                    null
                                )
                            )
                            (imagesRecycler.adapter as AddUnitImageAdapter).addImage(it)
                        }, {
                            Timber.e(it)
                            context?.handleApiError(it)
                        })
                    )
                }
            }
        },{
            Log.d("errror","errrorrrs")
        })



    }
}