package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Unit
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment

class ExperianceAdapter(private val units: List<Unit>, private val activity: Activity?) :
    RecyclerView.Adapter<ExperianceAdapter.ExperianceViewHolder>() {

    class ExperianceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val rate: TextView = itemView.findViewById(R.id.rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperianceViewHolder =
        ExperianceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.experiance_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = units.size

    override fun onBindViewHolder(holder: ExperianceViewHolder, position: Int) {
        activity?.let {
            if (units[position].attachments.isNotEmpty()) {
                Glide.with(it).load(units[position].attachments[0].image).into(holder.image)
            }
        }
        holder.title.text = units[position].title
        holder.rate.text = "${units[position].rateScore}"

        holder.itemView.setOnClickListener {
            activity?.let {
                (it as AppCompatActivity).supportFragmentManager.commit {
                    replace(
                        R.id.fragmentContainer,
                        UnitDetailsFragment.instance(units[position].id)
                    )
                    addToBackStack("")
                }
            }
        }
    }
}