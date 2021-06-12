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
import com.example.ezproject.data.models.ChatRoom
import com.example.ezproject.data.models.User
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.util.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ChatRoomsAdapter(
    private val activity: Activity?,
    private var chatRooms: List<ChatRoom>,
    private val userId: Int,
    private val onChatRoomClickListener: OnChatRoomClickListener
) :
    RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomViewHolder>() {

    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder =
        ChatRoomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_room_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = chatRooms.size

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        pickRemoteUser(chatRooms[position]).let { user ->
            Glide.with(activity!!).load("${Constants.STORAGE_URL}${user.avatar}")
                .placeholder(R.drawable.ic_account)
                .circleCrop()
                .into(holder.userImage)

            if (user.name!!.length >= 15) {
                holder.username.text = user.name.substring(0,15)
            }else{
                holder.username.text = user.name
            }



            if (chatRooms[position].chat.userId == userId) {
                holder.lastMsg.text = chatRooms[position].chat.message
                holder.lastMsg.visibility = View.VISIBLE
                holder.lastMsgRemote.visibility = View.GONE
            } else {
                holder.lastMsgRemote.text = chatRooms[position].chat.message
                holder.lastMsg.visibility = View.GONE
                holder.lastMsgRemote.visibility = View.VISIBLE
            }
            if (chatRooms[position].ownerId == userId) {
                if (chatRooms[position].unreadCount.ownerId > 0) {
                    holder.msgCount.text = "${chatRooms[position].unreadCount.ownerId}"
                    holder.msgCount.visibility = View.VISIBLE
                }
            } else {
                if (chatRooms[position].unreadCount.userId > 0) {
                    holder.msgCount.text = "${chatRooms[position].unreadCount.userId}"
                    holder.msgCount.visibility = View.VISIBLE
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        holder.lastMsgRemote.setTextAppearance(R.style.BoldText)
                    }
                }
            }




            holder.lastMsgDate.text = modifyTimeLayout(chatRooms[position].chat.createdAt)

//            var elaspsedTime: Long =
//                Calendar.getInstance().timeInMillis - dateFormatter.parse(chatRooms[position].chat.createdAt).time
//            elaspsedTime /= (1000 * 60)
//            elaspsedTime -= 114
//
//
//
//            if (elaspsedTime > 60) {
//                elaspsedTime /= 60
//                if (elaspsedTime > 24){
//                    elaspsedTime /= 24
//                    holder.lastMsgDate.text = "$elaspsedTime day ago"
//                }else{
//                    holder.lastMsgDate.text = "$elaspsedTime hr ago"
//
//                }
//            } else {
//                holder.lastMsgDate.text = "$elaspsedTime Min ago"
//            }




            holder.itemView.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.commit {
                    replace(R.id.fragmentContainer, ChatMessagesFragment().apply {
                        arguments =
                            bundleOf(Pair(Constants.CHAT_ROOM_ID_KEY, chatRooms[position].id))
                    })
                    addToBackStack("")
                }
            }
        }
    }


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

     fun modifyDateLayout(inputDate: String): String? {
        var inputDate = inputDate
        return try {
            inputDate = inputDate.replace(" UTC", "")
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(inputDate)
            SimpleDateFormat("dd-MM-yyyy").format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            "01-01-2010"
        }
    }
    fun setSearchResult(chatRoomsResult: List<ChatRoom>) {
        chatRooms = chatRoomsResult
        notifyDataSetChanged()
    }

    fun pickRemoteUser(chatRoom: ChatRoom): User {
        return if (userId == chatRoom.userId) chatRoom.owner else chatRoom.guest
    }

    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val username: TextView = itemView.findViewById(R.id.username)
        val lastMsg: TextView = itemView.findViewById(R.id.lastMsg)
        val lastMsgRemote: TextView = itemView.findViewById(R.id.lastMsgRemote)
        val msgCount: TextView = itemView.findViewById(R.id.msgCount)
        val lastMsgDate: TextView = itemView.findViewById(R.id.lastMsgDate)
    }

    interface OnChatRoomClickListener {
        fun onChatRoomClicked(chatRoom: ChatRoom)
    }
}