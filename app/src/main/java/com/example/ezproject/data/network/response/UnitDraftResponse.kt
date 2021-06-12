package com.example.ezproject.data.network.response

import com.google.gson.annotations.SerializedName

data class UnitDraftResponse(
    @SerializedName("unit")
    var unit: Unit,
    @SerializedName("options")
    var options: Options
) {
    data class Unit(
        @SerializedName("address")
        var address: String?,
        @SerializedName("allow_animals")
        var allowAnimals: String?,
        @SerializedName("allow_childrens")
        var allowChildrens: String?,
        @SerializedName("allow_extra")
        var allowExtra: String?,
        @SerializedName("allow_infants")
        var allowInfants: String?,
        @SerializedName("area")
        var area: String?,
        @SerializedName("attachments")
        var attachments: ArrayList<Attachment>,
        @SerializedName("balacons")
        var balacons: String?,
        @SerializedName("bank_account")
        var bankAccount: String?,
        @SerializedName("bank_number")
        var bankNumber: String?,
        @SerializedName("bathrooms")
        var bathrooms: String?,
        @SerializedName("beds")
        var beds: String?,
        @SerializedName("building_number")
        var buildingNumber: String?,
        @SerializedName("cancle_policy")
        var canclePolicy: String?,
        @SerializedName("checkin")
        var checkin: String?,
        @SerializedName("checkout")
        var checkout: String?,
        @SerializedName("child_type")
        var childType: String?,
        @SerializedName("city")
        var city: String?,
        @SerializedName("contract_image")
        var contractImage: String?,
        @SerializedName("country")
        var country: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("currency")
        var currency: String?,
        @SerializedName("dates")
        var dates: ArrayList<Date>,
        @SerializedName("days")
        var days: ArrayList<Day>,
        @SerializedName("deliver_keys")
        var deliverKeys: String?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("featured")
        var featured: String?,
        @SerializedName("fee")
        var fee: String?,
        @SerializedName("fee_static")
        var feeStatic: String?,
        @SerializedName("fees")
        var fees: ArrayList<Fee>,
        @SerializedName("floor_number")
        var floorNumber: String?,
        @SerializedName("get_keys")
        var getKeys: String?,
        @SerializedName("government")
        var government: String?,
        @SerializedName("guests")
        var guests: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("latitude")
        var latitude: String?,
        @SerializedName("longitude")
        var longitude: String?,
        @SerializedName("max_childrens")
        var maxChildrens: String?,
        @SerializedName("max_days")
        var maxDays: String?,
        @SerializedName("max_extra")
        var maxExtra: String?,
        @SerializedName("min_days")
        var minDays: String?,
        @SerializedName("min_guests")
        var minGuests: String?,
        @SerializedName("notes")
        var notes: String?,
        @SerializedName("options")
        var options: OptionsValue,
        @SerializedName("price")
        var price: String?,
        @SerializedName("promo")
        var promo: ArrayList<String>,
        @SerializedName("rate_count")
        var rateCount: String?,
        @SerializedName("rate_score")
        var rateScore: String?,
        @SerializedName("rooms")
        var rooms: String?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("tourism")
        var tourism: String?,
        @SerializedName("tourism_static")
        var tourismStatic: String?,
        @SerializedName("type")
        var type: String?,
        @SerializedName("unit_number")
        var unitNumber: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("user_id")
        var userId: String?,
        @SerializedName("vat")
        var vat: String?,
        @SerializedName("video")
        var video: String?,
        @SerializedName("zipcode")
        var zipcode: String?,
        @SerializedName("_method")
        var method: String? = "put"
    ) {
        data class OptionsValue(
            @SerializedName("aminites")
            var aminites: ArrayList<String>,
            @SerializedName("category")
            var category: ArrayList<String>,
            @SerializedName("country")
            var country: ArrayList<String>,
            @SerializedName("cpolicy")
            var cpolicy: ArrayList<String>,
            @SerializedName("fee")
            var fee: ArrayList<String>,
            @SerializedName("rest")
            var rest: ArrayList<String>,
            @SerializedName("views")
            var views: ArrayList<String>
        )
    }

    data class Options(
        var aminites: HashMap<String, String>,
        var views: HashMap<String, String>,
        var rest: HashMap<String, String>,
        var category: HashMap<String, String>,
        var country: HashMap<String, String>,
        var cpolicy: HashMap<String, ArrayList<String>>,
        var fee: HashMap<String, String>

    )

    data class Attachment(
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("ordr")
        var ordr: Int?,
        @SerializedName("thumb")
        var thumb: String?,
        @SerializedName("title")
        val title: String = "val",
        @SerializedName("unit_id")
        var unitId: Int?,
        @SerializedName("updated_at")
        var updatedAt: String?
    )

    data class Date(
        @SerializedName("date")
        var date: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("price")
        var price: String?,
        @SerializedName("price_before")
        var priceBefore: String?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("unit_id")
        var unitId: String?
    )

    data class Day(
        @SerializedName("created_at")
        var createdAt: String,
        @SerializedName("date_end")
        var dateEnd: String,
        @SerializedName("date_start")
        var dateStart: String,
        @SerializedName("day_price")
        var dayPrice: String,
        @SerializedName("day_price_before")
        var dayPriceBefore: String,
        @SerializedName("id")
        var id: String?,
        @SerializedName("unit_id")
        var unitId: String?,
        @SerializedName("updated_at")
        var updatedAt: String
    )

    data class Fee(
        @SerializedName("amount")
        var amount: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("fee_id")
        var feeId: String?,
        @SerializedName("fee_type")
        var feeType: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("unit_id")
        var unitId: String?,
        @SerializedName("updated_at")
        var updatedAt: String?
    )
}


