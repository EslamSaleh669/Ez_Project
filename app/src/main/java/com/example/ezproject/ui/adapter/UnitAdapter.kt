package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Unit
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment
import com.example.ezproject.util.extensions.roundPrice

class UnitAdapter(
    private val units: ArrayList<Unit>,
    private val currency: String,
    private val activity: Activity? ,
    private val checknum: Number
) :
    RecyclerView.Adapter<UnitAdapter.UnitViewHolder>() {


    class UnitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.unitImage)
        val unitName: TextView = itemView.findViewById(R.id.unitName)
        val unitPrice: TextView = itemView.findViewById(R.id.unitPrice)
        val unitRate: RatingBar = itemView.findViewById(R.id.unitRate)
        val unitTotalRate: TextView = itemView.findViewById(R.id.unitTotalRate)
        val unitPriceType: TextView = itemView.findViewById(R.id.unitPriceType)
        val offericon: ImageView = itemView.findViewById(R.id.offericon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder =

        if (checknum == 2){
            UnitViewHolder(

                LayoutInflater.from(parent.context).inflate(
                    R.layout.unit_item_layout,
                    parent,
                    false
                ) )
        }else{
            UnitViewHolder(

                LayoutInflater.from(parent.context).inflate(
                    R.layout.unit_item_layout2,
                    parent,
                    false
                ) )
        }




    override fun getItemCount(): Int = units.size
    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        activity?.let {
            if (units[position].attachments.isNotEmpty()) {
                Glide.with(it).load(units[position].attachments[0].image).into(holder.unitImage)
            }
        }

        if (units[position].title.length >= 15) {
            holder.unitName.text = units[position].title.substring(0, 15)
        } else {
            holder.unitName.text = units[position].title
        }
        if (units[position].type == 371 || units[position].type == 24|| units[position].type == 397){
            holder.unitPriceType.text = (activity?.getString(R.string.day))
        }else{
            holder.unitPriceType.text = (activity?.getString(R.string.night))

        }
        holder.unitPrice.text = "${units[position].price.roundPrice()} $currency"
        holder.unitTotalRate.text = "${units[position].rateCount}/5"
        holder.unitRate.rating = units[position].rateScore

        if (units[position].promotion > 0){
            holder.offericon.visibility = View.VISIBLE
        }
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

    fun addUnits(moreUnits: ArrayList<Unit>){
        this.units.addAll(moreUnits)
        notifyDataSetChanged()
    }
}