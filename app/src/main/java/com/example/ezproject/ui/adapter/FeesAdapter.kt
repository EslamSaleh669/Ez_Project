package com.example.ezproject.ui.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R

import com.example.ezproject.data.models.UnitDetails
import com.example.ezproject.util.Constants

class FeesAdapter (
    private val Fees: List<UnitDetails.Fee>,
    private val activity: Activity?
): RecyclerView.Adapter<FeesAdapter.FeesViewHolder>() {

    class FeesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Feename: TextView = itemView.findViewById(R.id.feename)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeesViewHolder =
        FeesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.feeslayout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = Fees.size

    override fun onBindViewHolder(holder: FeesViewHolder, position: Int) {

        val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(
            Constants.LANG_KEY,"en")

        if (lan.equals("ar")){
            holder.Feename.text =  Fees[position].taxonomy.name
        }else{
            holder.Feename.text =  Fees[position].taxonomy.nameEn

        }
            holder.Feename.background = activity!!.getDrawable(R.drawable.roundedshape)

    }
}


