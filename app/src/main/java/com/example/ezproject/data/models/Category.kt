package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("description_en")
    val descriptionEn: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("parent")
    val parent: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String
)