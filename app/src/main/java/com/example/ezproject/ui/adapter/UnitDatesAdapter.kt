package com.example.ezproject.ui.adapter

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.data.network.response.UnitDraftResponse
import kotlinx.android.synthetic.main.day_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class UnitDatesAdapter(
    private var unit: UnitDraftResponse.Unit,
    private val activity: Activity,
    private val onDateClicked: OnDateClicked
) :
    RecyclerView.Adapter<UnitDatesAdapter.UnitDateViewHolder>() {
    private val readDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val writeDateFormat = SimpleDateFormat("MMM d", Locale.UK)

    class UnitDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayDate: TextView = itemView.findViewById(R.id.dayDate)
        val oldPrice: TextView = itemView.findViewById(R.id.oldPrice)
        val newPrice: TextView = itemView.findViewById(R.id.newPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitDateViewHolder =
        UnitDateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.day_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = unit.dates.size

    override fun onBindViewHolder(holder: UnitDateViewHolder, position: Int) {
        if (unit.dates[position].status == "2") {
            holder.dayDate.setTextColor(activity.resources.getColor(R.color.red))
        } else {
            holder.dayDate.setTextColor(activity.resources.getColor(R.color.black))
        }
        val dayDate = readDateFormat.parse(unit.dates[position].date)
        holder.dayDate.text = writeDateFormat.format(dayDate.time)
        unit.dates[position].priceBefore?.let {
            holder.oldPrice.text = unit.dates[position].price
            holder.oldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.newPrice.text = unit.dates[position].priceBefore
        } ?: run {
            holder.newPrice.text = unit.dates[position].price
            holder.oldPrice.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (unit.dates[position].status != "2") {
                onDateClicked.onDateClicked(unit.dates[position])
            }
        }
    }


    fun updateData(unit: UnitDraftResponse.Unit) {
        this.unit = unit
        notifyDataSetChanged()
    }

    interface OnDateClicked {
        fun onDateClicked(date: UnitDraftResponse.Date)
    }
}