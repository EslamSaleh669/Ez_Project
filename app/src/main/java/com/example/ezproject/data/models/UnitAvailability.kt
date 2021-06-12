package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class UnitAvailability(
    @SerializedName("avg")
    val avg: Float,
    @SerializedName("avilable")
    val avilable: Float,
    @SerializedName("unit_tax_fee")
    val unitTaxFee: UnitTaxFee,
    @SerializedName("pay_price")
    val payPrice: Float
) {
    data class UnitTaxFee(
        @SerializedName("all_rent")
        val allRent: Float,
        @SerializedName("all_rent_seprate")
        val allRentSeprate: AllRentSeprate,
        @SerializedName("days_count")
        val daysCount: Float,
        @SerializedName("days_list")
        val daysList: List<String>,
        @SerializedName("ezuru")
        val ezuru: Ezuru,
        @SerializedName("fee")
        val fee: List<Fee>,
        @SerializedName("rent")
        val rent: Float,
        @SerializedName("tourism")
        val tourism: Tourism,
        @SerializedName("vat")
        val vat: Vat,
        @field:SerializedName("allOptionalFees")
         val allOptionalFees: List<AllOptionalFeesItem?>? = null

    ) {
        data class AllRentSeprate(
            @SerializedName("day_price")
            val dayPrice: Float,
            @SerializedName("ezuru")
            val ezuru: Float,
            @SerializedName("fee")
            val fee: Float,
            @SerializedName("rent")
            val rent: Float,
            @SerializedName("tourism")
            val tourism: Float,
            @SerializedName("vat")
            val vat: Float
        )

        data class Ezuru(
            @SerializedName("percent")
            val percent: Double,
            @SerializedName("static")
            val static: Float
        )

        data class Fee(
            @SerializedName("amount")
            val amount: Float,
            @SerializedName("fee")
            val fee: Fee
        ) {
            data class Fee(
                @SerializedName("amount")
                val amount: Float,
                @SerializedName("created_at")
                val createdAt: String,
                @SerializedName("fee_id")
                val feeId: Float,
                @SerializedName("fee_type")
                val feeType: String,
                @SerializedName("id")
                val id: Float,
                @SerializedName("taxonomy")
                val taxonomy: Taxonomy,
                @SerializedName("unit_id")
                val unitId: Float,
                @SerializedName("updated_at")
                val updatedAt: String
            ) {
                data class Taxonomy(
                    @SerializedName("created_at")
                    val createdAt: String,
                    @SerializedName("description")
                    val description: Any,
                    @SerializedName("description_en")
                    val descriptionEn: Any,
                    @SerializedName("id")
                    val id: Float,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("name_en")
                    val nameEn: String,
                    @SerializedName("parent")
                    val parent: Any,
                    @SerializedName("photo")
                    val photo: Any,
                    @SerializedName("status")
                    val status: Float,
                    @SerializedName("type")
                    val type: String,
                    @SerializedName("updated_at")
                    val updatedAt: String
                )
            }
        }

        data class Tourism(
            @SerializedName("percent")
            val percent: Double,
            @SerializedName("static")
            val static: Float
        )

        data class Vat(
            @SerializedName("percent")
            val percent: Double
        )

        data class AllOptionalFeesItem(

            @field:SerializedName("amount")
            val amount: Int? = null,

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
            val peopleLimit: Any? = null
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


    }
}