package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.ChatMessage
import com.example.ezproject.data.models.User
import com.example.ezproject.util.Constants


class ChatMessagesAdapter(
    private val activity: Activity?,
    private val messages: ArrayList<ChatMessage>,
    private val localUserId: Int,
     var otherPartyMember: User?
) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {

    class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatMsg: TextView = itemView.findViewById(R.id.chatMsg)
        val msgstatus: TextView = itemView.findViewById(R.id.msgstatus)
        val userImage: ImageView? = itemView.findViewById(R.id.userImage)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].userId == localUserId || messages[position].id == -1) Constants.CHAT_MSG_TYPE_USER
        else Constants.CHAT_MSG_TYPE_REMOTE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val viewId: Int = if (viewType == Constants.CHAT_MSG_TYPE_USER) {
            R.layout.user_chat_msg_item_layout
        } else {
            R.layout.remote_user_chat_msg_item_layout
        }

        return ChatMessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                viewId,
                parent,
                false
            )
        )

    }

    fun addMsg(msg: ChatMessage) {
        messages.add(msg)
        notifyItemInserted(messages.size - 1)

    }

    fun addMsgList(msgList: ArrayList<ChatMessage>) {
        messages.addAll(0, msgList.reversed())
        notifyItemRangeInserted(0, msgList.size)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.chatMsg.text = messages[position].message

        if (messages[position].userId == localUserId){
            if (messages[position].msgstatus == 0){
            holder.msgstatus.text = activity?.getString(R.string.reviewingmsg)
//            holder.msgstatus.setTextColor(activity?.resources!!.getColor(R.color.red))
        }else if (messages[position].msgstatus == 1){
            holder.msgstatus.text = activity?.getString(R.string.accepted)
//            holder.msgstatus.setTextColor(activity?.resources!!.getColor(R.color.white))
        }else if (messages[position].msgstatus == 2)
            holder.msgstatus.text = activity?.getString(R.string.rejectedmsg)
//            holder.msgstatus.setTextColor(activity?.resources!!.getColor(R.color.yellow))
        }



        otherPartyMember?.let { otherUser ->
            activity?.let {
                holder.userImage?.let {userImage->
                    Glide.with(it).load("${Constants.STORAGE_URL}${otherUser.avatar}")
                        .placeholder(R.drawable.ic_account).circleCrop().into(userImage)
                }

            }
        }
    }
}