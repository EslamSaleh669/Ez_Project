package com.example.ezproject.data.network.response

import com.example.ezproject.data.models.User
import com.google.gson.annotations.SerializedName


class CurrentUserResponse(
    @SerializedName("response")
    val user: User
)