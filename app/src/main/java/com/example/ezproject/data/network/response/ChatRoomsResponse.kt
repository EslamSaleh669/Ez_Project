package com.example.ezproject.data.network.response


import com.example.ezproject.data.models.ChatRoom
import com.google.gson.annotations.SerializedName

data class ChatRoomsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val chatRooms: ArrayList<ChatRoom>,
    @SerializedName("status")
    val status: Int
)