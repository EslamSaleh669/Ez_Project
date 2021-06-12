package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class PayPalPayment(
    @SerializedName("ACK")
    val aCK: String,
    @SerializedName("BUILD")
    val bUILD: String,
    @SerializedName("CORRELATIONID")
    val cORRELATIONID: String,
    @SerializedName("paypal_link")
    val paypalLink: String,
    @SerializedName("TIMESTAMP")
    val tIMESTAMP: String,
    @SerializedName("TOKEN")
    val tOKEN: String,
    @SerializedName("VERSION")
    val vERSION: String
)