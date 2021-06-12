package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("id")
    val id: String,
    @SerializedName("notifiable_id")
    val notifiableId: Int,
    @SerializedName("notifiable_type")
    val notifiableType: String,
    @SerializedName("read_at")
    val readAt: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String
) {
    data class Data(
        @SerializedName("date")
        val date: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("title")
        val title: String
    )
}