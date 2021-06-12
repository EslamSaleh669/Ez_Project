package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName
import com.example.ezproject.data.models.User

data class ChatRoom(
    @SerializedName("chat")
    val chat: ChatMessage,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("guest")
    val guest: User,
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner")
    val owner: User,
    @SerializedName("owner_id")
    val ownerId: Int,
    @SerializedName("unit_id")
    val unitId: Int,
    @SerializedName("unread_count")
    val unreadCount: UnreadCount,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
) {



    data class UnreadCount(
        @SerializedName("owner_id")
        val ownerId: Int,
        @SerializedName("user_id")
        val userId: Int
    )
}