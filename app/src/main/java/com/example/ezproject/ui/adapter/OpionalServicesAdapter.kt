package com.example.ezproject.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.data.models.OptionalServicesRef
import com.example.ezproject.util.Constants

class OpionalServicesAdapter(
    private val Services: ArrayList<OptionalServicesRef>,
    private val activity: Activity?,
    private val onOptionalServClick: OnOptionalServClick

): RecyclerView.Adapter<OpionalServicesAdapter.OpionalServicesVholder>() {

    class OpionalServicesVholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val servcheck = itemView.findViewById<CheckBox>(R.id.servicecheck)
        val servMin = itemView.findViewById<ImageView>(R.id.serviceMinusBtn)
        val servPlus = itemView.findViewById<ImageView>(R.id.servicePlusBtn)
        val servCount = itemView.findViewById<EditText>(R.id.serviceCount)
        val ServTitle = itemView.findViewById<TextView>(R.id.serviceTitle)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpionalServicesVholder =
        OpionalServicesVholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.optional_service_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = Services!!.size

    override fun onBindViewHolder(holder: OpionalServicesVholder, position: Int) {

        val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(
            Constants.LANG_KEY,"en")
        val currency = activity?.getSharedPreferences(Constants.SHARED_NAME,Context.MODE_PRIVATE)?.getString (Constants.CURRENCY_KEY,"egp")


        if (lan.equals("ar")) {
            if (Services[position].feeType.equals("percent")){

                holder.ServTitle.text = "${Services[position].name} (${Services[position].amount}%)"
            }else{
                holder.ServTitle.text = "${Services[position].name} (${Services[position].amount}${currency})"

            }
        }else{
            if (Services[position].feeType.equals("percent")){

                holder.ServTitle.text = "${Services[position].nameen} (${Services[position].amount}%)"
            }else{
                holder.ServTitle.text = "${Services[position].nameen} (${Services[position].amount}${currency})"

            }
        }



        holder.servCount.setText(Services[position].count)
        holder.servcheck.isChecked = Services[position].checked!!


        holder.servPlus.setOnClickListener {
            val count =  holder.servCount.text.toString().toInt() + 1
            holder.servCount.setText("$count")

            onOptionalServClick.ServCount(Services[position]!!.feeId!!,count.toString())
        }
        holder.servMin.setOnClickListener {
            val count =  holder.servCount.text.toString().toInt() - 1
            if (count >= 0)
                holder.servCount.setText("$count")
            onOptionalServClick.ServCount(Services[position]!!.feeId!!,count.toString())


        }

        holder.servcheck.setOnClickListener {

            onOptionalServClick.ServCheck(Services[position]!!.feeId!!,holder.servcheck.isChecked,position)

        }
    }

    public interface OnOptionalServClick {
        fun ServCount(position: Int , count: String)
        fun ServCheck(feeid: Int , checked: Boolean , position: Int)
    }
}


