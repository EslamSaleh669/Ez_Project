package com.example.ezproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.data.models.PayIn

class PayInHistoryAdapter(
    private val payIns: List<PayIn>,
    private val currency: String
) :
    RecyclerView.Adapter<PayInHistoryAdapter.PayInItemViewHolder>() {

    class PayInItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val paymentDate: TextView = itemView.findViewById(R.id.paymentDate)
        val unitTile: TextView = itemView.findViewById(R.id.unitTile)
        val paymentAmount: TextView = itemView.findViewById(R.id.paymentAmount)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayInItemViewHolder =
        PayInItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pay_in_history_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = payIns.size

    override fun onBindViewHolder(holder: PayInItemViewHolder, position: Int) {


        holder.paymentDate.text = payIns[position].objectX.updatedAt.split(" ")[0]
        holder.unitTile.text = payIns[position].unit.title
        holder.paymentAmount.text = "${payIns[position].amount} $currency"
        holder.createdAt.text = payIns[position].objectX.updatedAt.split(" ")[0]
        holder.status.text = when (payIns[position].amountStatus) {
            "2" -> {
                "Paid"
            }
            "3" -> {
                "check in"
            }
            "4" -> {
                "check out"
            }
            "5" -> {
                "key delivered"
            }
            "6" -> {
                "confirm key delivered"
            }
            "-2" -> {
                "cancelled request"
            }
            "-3" -> {
                "cancel acceptance"
            }
            else -> ""
        }

    }
}