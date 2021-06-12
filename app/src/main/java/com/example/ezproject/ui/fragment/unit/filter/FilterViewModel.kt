package com.example.ezproject.ui.fragment.unit.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.*
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.network.response.FilterOptionsResponse
import com.example.ezproject.data.network.response.UnitsResponse
import com.example.ezproject.data.repo.UnitRepo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject

class FilterViewModel(private val unitRepo: UnitRepo) : ViewModel() {
    private var filterOptions = FilterOptions()
    var page: Int = 1
    var limit = 0
    var units: ReplaySubject<ArrayList<Unit>> = ReplaySubject.create()
    var hotels: ReplaySubject<ArrayList<Hotel>> = ReplaySubject.create()
    var disposable: Disposable? = null
    val currentCurrency by lazy {
        unitRepo.currentCurrency().toUpperCase()
    }

    fun setOriginId(originId: Int?) {
        filterOptions.originId = originId
    }

    fun setHotelUser(uid: Int?) {
        filterOptions.userid = uid
    }

    fun setHotelownerid(ownerid: Int?) {
        filterOptions.owner_id = ownerid
    }



    fun filterItems(type: String): Observable<FilterOptionsResponse> =
        unitRepo.filterOptions(type)




    fun add2Amenities(id: Int) {
        if (filterOptions.amenities.contains(id)) {
            filterOptions.amenities.remove(id)
        } else {
            filterOptions.amenities.add(id)
        }
    }

    fun add2Rest(id: Int) {
        if (filterOptions.rest.contains(id)) {
            filterOptions.rest.remove(id)
        } else {
            filterOptions.rest.add(id)
        }
    }

    fun add2Category(id: Int) {
        if (filterOptions.category.contains(id)) {
            filterOptions.category.remove(id)
        } else {
            filterOptions.category.add(id)
        }
    }

    fun setCategoryId(id: Int) {
        filterOptions.category.clear()
        filterOptions.category.add(id)
    }

    fun clearFilters() {
        filterOptions = FilterOptions()
    }

    fun setFilterCounts(
        adultsCount: String,
        childCount: String,
        bedsCount: String,
        roomsCount: String,
        bathCount: String,
        originId: Int?
    ) {
        filterOptions.guestsCount = adultsCount
        filterOptions.childCount = childCount
        filterOptions.roomsCount = roomsCount
        filterOptions.bedsCount = bedsCount
        filterOptions.bathCount = bathCount
        filterOptions.originId = originId
    }

    fun addFilterDate(checkIn: String, checkOut: String) {
        filterOptions.checkIn = checkIn
        filterOptions.checkOut = checkOut
    }

    fun loadFilteredUnits() {
        disposable = unitRepo.filteredUnits(this.filterOptions, page).subscribe({
            page++
            limit += it.response.units.size
            units.onNext(it.response.units)
        }, {
            units.onError(it)
        })
    }


    fun loadFilteredHotels() {
        disposable = unitRepo.filtereHotels(this.filterOptions , page).subscribe({
            page++
            limit += it.data.size
            hotels.onNext(it.data)
        }, {
            hotels.onError(it)
         })
    }

    fun getFilteredHotels(): Observable<AllCorporatesResponse> {

         return   unitRepo.AllfilteredHotelss(this.filterOptions, page)

    }



    fun addPrices(startPrice: Float, endPrice: Float) {
        filterOptions.prices = ArrayList()
        filterOptions.prices.add(startPrice)
        filterOptions.prices.add(endPrice)
    }

    fun setOrderBy(orderBy: String) {
        filterOptions.orderby = orderBy
    }


    fun checkForUnitsLoading(lastPosition: Int) {
        var currentItemsCount = 0
        for (item in units.values) {
            currentItemsCount += (item as ArrayList<*>).size
        }
        if (currentItemsCount - 1 == lastPosition) {
            loadFilteredUnits()
        }
    }

    fun checkForhotelsLoading(lastPosition: Int) {
        var currentItemsCount = 0
        for (item in hotels.values) {
            currentItemsCount += (item as ArrayList<*>).size
        }
        if (currentItemsCount - 1 == lastPosition) {
            loadFilteredHotels()
        }
    }



//
//    fun checkUnitAvailable(
//        unitId: Int,
//        startDate: String,
//        endDate: String
//    ): Observable<UnitAvailability2> =
//        apiClient.checkUnitAvailable(
//            userRepo.currentLang(),
//            userRepo.currentCurrency(),
//            "Bearer ${userRepo.readUserData()!!.token}",
//            unitId,
//            startDate,
//            endDate
//        ).subscribeOn(Schedulers.io())

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}
