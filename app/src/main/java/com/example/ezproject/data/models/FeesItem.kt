package com.example.ezproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class FeesItem(

	@Expose
	@field:SerializedName("peopleLimit")
	var peopleLimit: String? = null,

	@Expose
	@field:SerializedName("fee_id")
	var feeId: String? = null
)
