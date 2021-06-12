package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class GovernmentResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: List<Response>,
    @SerializedName("status")
    val status: Int
) {
    data class Response(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: Any,
        @SerializedName("description_en")
        val descriptionEn: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_en")
        val nameEn: String,
        @SerializedName("parent")
        val parent: Int,
        @SerializedName("photo")
        val photo: Any,
        @SerializedName("status")
        val status: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) {
        override fun toString(): String {
            return this.name
        }
    }
}