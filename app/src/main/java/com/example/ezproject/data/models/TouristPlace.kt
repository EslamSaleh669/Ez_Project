package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class TouristPlace(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("description_en")
    val descriptionEn: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("taxonomy_id")
    val taxonomyId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_en")
    val titleEn: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("views")
    val views: Int,
    @SerializedName("origin_id")
    val originId: Int
)