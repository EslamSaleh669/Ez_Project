package com.example.ezproject.data.network.response


import com.example.ezproject.data.models.ChatMessage
import com.example.ezproject.data.models.User
import com.google.gson.annotations.SerializedName

data class ChatRoomMessagesResponse(
    @SerializedName("chat")
    val chat: Chat,
    @SerializedName("message")
    val message: Message
) {
    data class Chat(
        @SerializedName("current_page")
        val currentPage: Int,
        @SerializedName("data")
        val messages: ArrayList<ChatMessage>,
        @SerializedName("first_page_url")
        val firstPageUrl: String,
        @SerializedName("from")
        val from: Int,
        @SerializedName("last_page")
        val lastPage: Int,
        @SerializedName("last_page_url")
        val lastPageUrl: String,
        @SerializedName("next_page_url")
        val nextPageUrl: String,
        @SerializedName("path")
        val path: String,
        @SerializedName("per_page")
        val perPage: Int,
        @SerializedName("prev_page_url")
        val prevPageUrl: Any,
        @SerializedName("to")
        val to: Int,
        @SerializedName("total")
        val total: Int
    )

    data class Message(
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
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("status")
        val msgstatus: Int
    )
}