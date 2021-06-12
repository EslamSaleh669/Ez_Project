package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class UnitReportResponse(
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
        @SerializedName("flagged_by")
        val flaggedBy: Int,
        @SerializedName("flagged_id")
        val flaggedId: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}