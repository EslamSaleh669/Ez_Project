package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("url")
    var url: String
)