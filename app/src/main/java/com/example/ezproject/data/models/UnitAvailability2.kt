package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName
import io.reactivex.internal.operators.flowable.FlowableTakeLastOne

data class UnitAvailability2(

	@field:SerializedName("unit_tax_fee")
	val unitTaxFee: UnitTaxFee? = null,

	@field:SerializedName("pay_price")
	val payPrice: Float? = null,

	@field:SerializedName("avg")
	val avg: Float? = null,

	@field:SerializedName("min_max")
	val minMax: MinMax? = null,

	@field:SerializedName("daysType")
	val daysType: String? = null,

	@field:SerializedName("avilable")
	val avilable: Int? = null
)

data class Ezuru(

	@field:SerializedName("static")
	val jsonMemberStatic: Int? = null,

	@field:SerializedName("percent")
	val percent: Double? = null
)

data class RequiredFeeItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("perPerson")
	val perPerson: String? = null,

	@field:SerializedName("fee")
	val fee: Fee? = null
)

data class AllOptionalFeesItem(

	@field:SerializedName("amount")
	val amount: Float? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("selectable")
	val selectable: String? = null,

	@field:SerializedName("fee_id")
	val feeId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fee_type")
	val feeType: String? = null,

	@field:SerializedName("taxonomy")
	val taxonomy: Taxonomy? = null,

	@field:SerializedName("unit_id")
	val unitId: Int? = null,

	@field:SerializedName("peopleLimit")
	val peopleLimit: String? = null
)

data class Tourism(

	@field:SerializedName("static")
	val jsonMemberStatic: Int? = null,

	@field:SerializedName("percent")
	val percent: Int? = null
)

data class SelectedOptionalFeesItem(

	@field:SerializedName("amount")
	val amount: Float? = null,

	@field:SerializedName("perPerson")
	val perPerson: String? = null,

	@field:SerializedName("fee")
	val fee: Fee? = null
)

data class Vat(

	@field:SerializedName("percent")
	val percent: Int? = null
)

data class MinMax(

	@field:SerializedName("min")
	val min: Int? = null,

	@field:SerializedName("max")
	val max: Int? = null
)

data class Fee(

	@field:SerializedName("amount")
	val amount: Float? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("selectable")
	val selectable: String? = null,

	@field:SerializedName("fee_id")
	val feeId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fee_type")
	val feeType: String? = null,

	@field:SerializedName("taxonomy")
	val taxonomy: Taxonomy? = null,

	@field:SerializedName("unit_id")
	val unitId: Int? = null,

	@field:SerializedName("peopleLimit")
	val peopleLimit: String? = null
)

data class UnitTaxFee(

	@field:SerializedName("allOptionalFees")
	val allOptionalFees: List<AllOptionalFeesItem?>? = null,

	@field:SerializedName("requiredFee")
	val requiredFee: List<RequiredFeeItem?>? = null,

	@field:SerializedName("all_rent_seprate")
	val allRentSeprate: AllRentSeprate? = null,

	@field:SerializedName("vat")
	val vat: Vat? = null,

	@field:SerializedName("tourism")
	val tourism: Tourism? = null,

	@field:SerializedName("all_rent")
	val allRent: Float? = null,

	@field:SerializedName("selectedOptionalFees")
	val selectedOptionalFees: List<SelectedOptionalFeesItem?>? = null,

	@field:SerializedName("days_count")
	val daysCount: Int? = null,

	@field:SerializedName("ezuru")
	val ezuru: Ezuru? = null,

	@field:SerializedName("days_list")
	val daysList: List<String?>? = null,

	@field:SerializedName("rent")
	val rent: Float? = null
)

data class AllRentSeprate(

	@field:SerializedName("fee")
	val fee: Float? = null,

	@field:SerializedName("vat")
	val vat: Float? = null,

	@field:SerializedName("tourism")
	val tourism: Float? = null,

	@field:SerializedName("ezuru")
	val ezuru: Float? = null,

	@field:SerializedName("rent")
	val rent: Float? = null,

	@field:SerializedName("day_price")
	val dayPrice: Float? = null
)

data class Taxonomy(

	@field:SerializedName("parent")
	val parent: Any? = null,

	@field:SerializedName("description_en")
	val descriptionEn: Any? = null,

	@field:SerializedName("region_id")
	val regionId: Any? = null,

	@field:SerializedName("description")
	val description: Any? = null,

	@field:SerializedName("photo")
	val photo: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("taxArrange")
	val taxArrange: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name_en")
	val nameEn: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
