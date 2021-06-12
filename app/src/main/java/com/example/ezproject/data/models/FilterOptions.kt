package com.example.ezproject.data.models

class FilterOptions(
    var checkIn: String? = null,
    var checkOut: String? = null,
    var guestsCount: String? = null,
    var roomsCount: String? = null,
    var bedsCount: String? = null,
    var bathCount: String? = null,
    var originId: Int? = null,
    var userid: Int? = null,
    var owner_id: Int? = null,
    var childCount: String? = null,
    var orderby:String = "DATE",
    var amenities: ArrayList<Int> = ArrayList(),
    var rest: ArrayList<Int> = ArrayList(),
    var category: ArrayList<Int> = ArrayList(),
    var prices: ArrayList<Float> = ArrayList()

) {
}