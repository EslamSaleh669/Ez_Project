package com.example.ezproject.ui.adapter

import android.app.Activity
import android.content.SharedPreferences
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
import com.example.ezproject.data.models.TouristPlace
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.util.Constants

class TouristPlacesAdapter(private val places: List<TouristPlace>, val activity: Activity?) :
    RecyclerView.Adapter<TouristPlacesAdapter.TouristPlacesViewHolder>() {


    class TouristPlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage)
        val placeName: TextView = itemView.findViewById(R.id.placeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TouristPlacesViewHolder =
        TouristPlacesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tourist_places_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: TouristPlacesViewHolder, position: Int) {
        holder.placeName.text = places[position].title
        activity?.let {
            Glide.with(it).load(places[position].photo).into(holder.placeImage)
        }


        holder.itemView.setOnClickListener {

            val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences( Constants.preferences_fileName,0)
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putInt("selid", places[position].originId)
            editor.apply()
            (activity as AppCompatActivity).supportFragmentManager.commit {
                replace(R.id.fragmentContainer,AllUnitsFragment())
                addToBackStack("")
            }
        }
    }
}