package com.example.ezproject.data.network.response

import com.example.ezproject.data.models.User
import com.google.gson.annotations.SerializedName

data class UserUpdateResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val user: User,
    @SerializedName("status")
    val status: Int
)