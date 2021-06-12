package com.example.ezproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.data.models.Payout
import com.example.ezproject.util.extensions.roundPrice

class PayoutHistoryAdapter(
    private val payouts: List<Payout>,
    private val currency: String
) :
    RecyclerView.Adapter<PayoutHistoryAdapter.PaymentItemViewHolder>() {

    class PaymentItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val paymentDate: TextView = itemView.findViewById(R.id.paymentDate)
        val unitTile: TextView = itemView.findViewById(R.id.unitTile)
        val paymentAmount: TextView = itemView.findViewById(R.id.paymentAmount)
        val fee: TextView = itemView.findViewById(R.id.fee)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentItemViewHolder =
        PaymentItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.payout_history_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = payouts.size

    override fun onBindViewHolder(holder: PaymentItemViewHolder, position: Int) {

        holder.paymentDate.text = payouts[position].objectX.updatedAt.split(" ")[0]
        holder.unitTile.text = payouts[position].unit.title
        holder.paymentAmount.text = "${payouts[position].amount.amount.roundPrice()} $currency"
        holder.fee.text = "${payouts[position].amount.fee} $currency"
        holder.status.text = when (payouts[position].amountStatus) {
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