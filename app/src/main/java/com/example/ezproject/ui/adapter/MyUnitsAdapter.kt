package com.example.ezproject.ui.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Unit
import com.example.ezproject.ui.fragment.unit.add.BasicInfoFragment
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment


class MyUnitsAdapter(
    private val units: ArrayList<Unit>,
    private val activity: Activity?,
    private val currency:String,
    private val onDelCLickListener: MyUnitsAdapter.OnDelCLickListener


    ) :
    RecyclerView.Adapter<MyUnitsAdapter.MyUnitViewHolder>() {


    class MyUnitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.unitImage)
        val unitTitle: TextView = itemView.findViewById(R.id.unitTitle)
        val unitAddress: TextView = itemView.findViewById(R.id.unitAddress)
        val unitPric: TextView = itemView.findViewById(R.id.unitPrice)
        val unitdelete: ImageView = itemView.findViewById(R.id.unitdelete)

//        val unitBookingCount: TextView = itemView.findViewById(R.id.unitBookingCount)
//        val editUnit: FrameLayout = itemView.findViewById(R.id.editUnit)
//        val isAccepted: TextView = itemView.findViewById(R.id.isAccepted)
//        val isPublished: TextView = itemView.findViewById(R.id.isPublished)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyUnitViewHolder =
        MyUnitViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.my_unit_item_layout,
                parent,
                false
            )
        )


    fun removeItem(unitId: Int) {
        for (unit in units) {
            if (unit.id == unitId) {
                units.remove(unit)

                break
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = units.size

    override fun onBindViewHolder(holder: MyUnitViewHolder, position: Int) {
        activity?.let {
            if (units[position].attachments.isNotEmpty()) {
                Glide.with(activity).load(units[position].attachments[0].image)
                    .into(holder.unitImage)
            }

        }

/*
        holder.isPublished.text = when (units[position].status) {
            0 -> {
                "Under Revision"
            }
            1 -> {
                "published"
            }
            2 -> {
                "Pending"
            }
            else -> {
                ""
            }
        }
*/

        if (units[position].title.length > 25){
            holder.unitTitle.text = units[position].title.substring(0,25)

        }else{
            holder.unitTitle.text = units[position].title

        }

        if (units[position].address.length > 25){
            holder.unitAddress.text = units[position].address.substring(0,25)

        }else{
            holder.unitAddress.text = units[position].address

        }

        holder.unitPric.text = "${units[position].price} $currency"
//        holder.unitBookingCount.text = "Booking: ${units[position].bookingCount}"
//        holder.itemView.setOnClickListener {
//            (activity as AppCompatActivity).supportFragmentManager.commit {
//                replace(R.id.fragmentContainer, UnitDetailsFragment.instance(units[position].id))
//                addToBackStack("")
//            }
//        }

        holder.unitdelete.setOnClickListener {

            onDelCLickListener.onDelClicked(units[position].id)

        }
         holder.itemView.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.commit {
                replace(R.id.fragmentContainer, BasicInfoFragment().apply {
                    arguments = bundleOf(Pair("id", units[position].id))
                })
                addToBackStack("")
            }
        }
    }

    public interface OnDelCLickListener {
        fun onDelClicked(unitId: Int)
    }
}