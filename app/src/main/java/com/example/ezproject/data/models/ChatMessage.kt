package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("message_id")
    val messageId: Int,
    @SerializedName("readed")
    val readed: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("status")
    val msgstatus: Int
)