package com.example.ezproject.ui.fragment.main

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.HotelDesResponse
import com.example.ezproject.data.models.SearchPlaces
import com.example.ezproject.data.models.User
import com.example.ezproject.data.network.response.CategoryReponse
import com.example.ezproject.data.network.response.TouristPlacesResponse
import com.example.ezproject.data.network.response.UnitsResponse
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class MainViewModel(private val userRepo: UserRepo, private val unitRepo: UnitRepo) : ViewModel() {
    val currentCurrency by lazy {
        userRepo.currentCurrency().toUpperCase()
    }
    fun categories(): Observable<CategoryReponse> =
        unitRepo.categories(1)


    fun categories_Hotels(): Observable<CategoryReponse> =
        unitRepo.categories_hotels(1)

    fun units(): Observable<UnitsResponse> = unitRepo.units(1)
    fun featuredUnits(): Observable<UnitsResponse> = unitRepo.featuredUnits(1)

    fun recommendedUnits(): Observable<UnitsResponse> = unitRepo.recommendedUnits(1)

    fun places(type: String): Observable<TouristPlacesResponse> = unitRepo.places(1, type)

    fun userData(): User? = userRepo.readUserData()

    fun topExperienced(): Observable<UnitsResponse> = unitRepo.topExperianced(1)

    fun searchPlaces(search: String): Observable<List<SearchPlaces>> = unitRepo.searchPlaces(search)

    fun hotelpopuardestnations(): Observable<List<HotelDesResponse>> = unitRepo.Hotelplaces()

}