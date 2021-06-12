package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class VerifyEmailResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: Response,
    @SerializedName("status")
    val status: Int
) {
    data class Response(
        @SerializedName("result")
        val result: Boolean
    )
}