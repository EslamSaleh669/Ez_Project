package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.data.models.Notification
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val activity: Activity?,
    private val notifications: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val notificationTitle: TextView = itemView.findViewById(R.id.notificationTitle)
        val notificationContent: TextView = itemView.findViewById(R.id.notificationContent)
        val notificationTime: TextView = itemView.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.notification_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = notifications.size

    fun modifyTimeLayout(inputDate: String): String? {
        var inputDate = inputDate
        return try {
            inputDate = inputDate.replace(" UTC", "")
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(inputDate)
            SimpleDateFormat("dd,MMM  hh:mm a").format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            "01-01-2010"
        }
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.notificationTitle.text = notifications[position].data.title
        holder.notificationContent.text = notifications[position].data.message
        Timber.d("${notifications[position].data.date}")

        holder.notificationTime.text = modifyTimeLayout(notifications[position].createdAt)
//        var elaspsedTime: Long =
//            Calendar.getInstance().timeInMillis - dateFormatter.parse(notifications[position].createdAt).time
//        elaspsedTime /= (1000 * 60 * 60)
//        if (elaspsedTime >= 24) {
//            elaspsedTime /= 24
//            holder.notificationTime.text = "$elaspsedTime D"
//
//        } else {
//            holder.notificationTime.text = "$elaspsedTime H"
//        }

    }
}