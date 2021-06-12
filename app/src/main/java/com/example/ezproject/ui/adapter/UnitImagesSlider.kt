package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.models.UnitDetails
import com.smarteist.autoimageslider.SliderViewAdapter

class UnitImagesSlider(
    private val activity: Activity?,
    private val images: List<UnitDetails.Attachment>
) : SliderViewAdapter<UnitImagesSlider.UnitImageViewHolder>() {
    class UnitImageViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val sliderImage: ImageView = itemView.findViewById(R.id.sliderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): UnitImageViewHolder =
        UnitImageViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.slider_image,
                parent,
                false
            )
        )

    override fun getCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: UnitImageViewHolder?, position: Int) {
        activity?.let {
            Glide.with(it).load(images[position].image).into(viewHolder?.sliderImage!!)
        }
    }
}