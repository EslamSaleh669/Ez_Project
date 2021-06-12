package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.UnitDetails

class AmentiesAdapter(
    private val items: List<UnitDetails.Amenity>,
    private val activity: Activity?
) : RecyclerView.Adapter<AmentiesAdapter.AmentiesViewHolder>() {

    class AmentiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView = itemView.findViewById(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmentiesViewHolder =
        AmentiesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.amenties_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AmentiesViewHolder, position: Int) {
        activity?.let {
            Glide.with(it).load(items[position].photo).into(holder.itemImg)
        }
    }
}