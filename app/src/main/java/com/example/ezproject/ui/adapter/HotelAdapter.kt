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
import com.example.ezproject.data.models.Hotel
import com.example.ezproject.data.models.Unit
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment
import com.example.ezproject.util.extensions.roundPrice

class HotelAdapter(
    private val hotels: ArrayList<Hotel>,
    private val currency: String,
    private val activity: Activity? ,
    private val checknum: Number
) :
    RecyclerView.Adapter<HotelAdapter.HotelVHolder>() {


    class HotelVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.unitImage)
        val unitName: TextView = itemView.findViewById(R.id.unitName)
        val unitPrice: TextView = itemView.findViewById(R.id.unitPrice)
        val unitRate: RatingBar = itemView.findViewById(R.id.unitRate)
        val unitTotalRate: TextView = itemView.findViewById(R.id.unitTotalRate)
        val unitPriceType: TextView = itemView.findViewById(R.id.unitPriceType)
        val offericon: ImageView = itemView.findViewById(R.id.offericon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelVHolder =

            HotelVHolder(

                LayoutInflater.from(parent.context).inflate(
                    R.layout.unit_item_layout,
                    parent,
                    false
                ) )




    override fun getItemCount(): Int = hotels.size
    override fun onBindViewHolder(holder: HotelVHolder, position: Int) {
        activity?.let {
                Glide.with(it).load(hotels[position].contractImage).
                placeholder(R.drawable.ic_ezuru_luancher).error(R.drawable.ic_ezuru_luancher).into(holder.unitImage)

        }

        if (hotels[position].title.length >= 15) {
            holder.unitName.text = hotels[position].title.substring(0, 15)
        } else {
            holder.unitName.text = hotels[position].title
        }

        holder.unitPriceType.text = (activity?.getString(R.string.night))


        holder.unitPrice.text = "${hotels[position].price} $currency"
        holder.unitTotalRate.text = "${hotels[position].rateCount}/5"
        holder.unitRate.rating = hotels[position].rateScore.toFloat()

        if (hotels[position].promotion > 0){
            holder.offericon.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            activity?.let {
                (it as AppCompatActivity).supportFragmentManager.commit {
                    replace(
                        R.id.fragmentContainer,
                        UnitDetailsFragment.instance(hotels[position].id)
                    )
                    addToBackStack("")
                }
            }
        }
    }

    fun addHotels(morehotels: ArrayList<Hotel>){
        this.hotels.addAll(morehotels)
        notifyDataSetChanged()
    }
}