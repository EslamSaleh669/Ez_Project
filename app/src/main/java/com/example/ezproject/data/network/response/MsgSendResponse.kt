package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class MsgSendResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: Response,
    @SerializedName("status")
    val status: Int
) {
    data class Response(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("owner_id")
        val ownerId: String,
        @SerializedName("unit_id")
        val unitId: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: String
    )
}