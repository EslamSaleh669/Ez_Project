package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.DataItem
import com.example.ezproject.ui.fragment.AllHotelssFragment
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.util.Constants


class AllCorporateAdapter(

    private val corporates: List<DataItem>,
    private val currency: String,
    private val activity: Activity
)
    : RecyclerView.Adapter<AllCorporateAdapter.CorpViewHolder>() {


    class CorpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.cImage)
        val unitName: TextView = itemView.findViewById(R.id.ctitle)
        val unitPrice: TextView = itemView.findViewById(R.id.cprice)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorpViewHolder =

            CorpViewHolder(

                LayoutInflater.from(parent.context).inflate(
                    R.layout.corporate_layout,
                    parent,
                    false
                ) )






    override fun getItemCount(): Int = corporates.size
    override fun onBindViewHolder(holder: CorpViewHolder, position: Int) {
        activity.let {
            Glide.with(it).load(corporates[position].hotelPhoto).placeholder(R.drawable.ic_ezuru_luancher)
                .error(R.drawable.ic_ezuru_luancher).into(holder.unitImage)

        }

        if (corporates[position].name.length >= 15) {
            holder.unitName.text = corporates[position].name.substring(0, 15)
        } else {
            holder.unitName.text = corporates[position].name
        }

        holder.unitPrice.text = "${corporates[position].minimumPrice} $currency"


        holder.itemView.setOnClickListener {
            activity.let {
                (it as AppCompatActivity).supportFragmentManager.commit {

                    replace(R.id.fragmentContainer,
                        AllHotelssFragment().apply {
                            arguments = bundleOf(Pair(Constants.HOTELID, corporates[position].id))
                        }
                    ).addToBackStack("")

                }
            }


        }
    }


}