package com.example.ezproject.ui.adapter

import android.app.Activity
import android.content.Context
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
import com.example.ezproject.data.models.HotelDesResponse
import com.example.ezproject.data.models.TouristPlace
import com.example.ezproject.ui.fragment.AllCorporatesFragment
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.util.Constants

class HotelPopularPAdapter(private val places: List<HotelDesResponse>, val activity: Activity?): RecyclerView.Adapter<HotelPopularPAdapter.HotelPopVholder>() {



    class HotelPopVholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage)
        val placeName: TextView = itemView.findViewById(R.id.placeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelPopVholder =
        HotelPopVholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tourist_places_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: HotelPopVholder, position: Int) {

        val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(Constants.LANG_KEY,"en")

        if (lan.equals("ar")){
            holder.placeName.text = places[position].name
        }else{
            holder.placeName.text = places[position].nameEn

        }
        activity?.let {
            Glide.with(it).load(places[position].photo).placeholder(R.drawable.ic_ezuru_luancher)
                .error(R.drawable.ic_ezuru_luancher)
                .into(holder.placeImage)
        }


        holder.itemView.setOnClickListener {

            val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences( Constants.preferences_fileName,0)
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putInt("selid", places[position].id)
            editor.apply()
            (activity as AppCompatActivity).supportFragmentManager.commit {

                val sharedPreferencess: SharedPreferences = activity.getSharedPreferences(
                    Constants.Tosaveit,
                    0
                )
                val editorr: SharedPreferences.Editor = sharedPreferencess.edit()
                editorr.putInt("categoryid",places[position].id)
                editorr.putInt("myplace",2)
                editorr.apply()

                replace(R.id.fragmentContainer,
                    AllCorporatesFragment().apply {
                        arguments = bundleOf(
                            Pair(Constants.CATE_ID_KEY, places[position].id),
                            Pair(Constants.FROMWHERE, 2)

                        )
                    }
                )
                addToBackStack("")
            }
        }
    }

}