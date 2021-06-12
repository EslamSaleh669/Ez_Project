package com.example.ezproject.data.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FeesListRequest(

    @field:SerializedName("date_start")
    var dateStart: String? = null,

    @field:SerializedName("date_end")
    var dateEnd: String? = null,

    @field:SerializedName("unit_id")
    var unitId: String? = null,


    @field:SerializedName("fees")
    var fees: List<FeesItem?>? = null
)