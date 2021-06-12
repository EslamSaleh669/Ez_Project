package com.example.ezproject.ui.fragment.unit.favourite

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.network.response.UnitsResponse
import com.example.ezproject.data.network.response.WishListResponse
import com.example.ezproject.data.network.response.WishlistAddResponse
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class FavouriteViewModel(private val unitRepo: UnitRepo, private val userRepo: UserRepo) :
    ViewModel() {

    val currentCurrency by lazy {
        unitRepo.currentCurrency().toUpperCase()
    }

    fun favourites(): Observable<WishListResponse> = unitRepo.myWishList()

    fun removeFrmFav(unitId: Int): Observable<WishlistAddResponse> =
        unitRepo.addUnit2WishList(unitId)

    fun removeFromLocalFav(unitId: Int) {
        userRepo.addItem2Fav(unitId)
    }

    fun addFavSet(favs: MutableSet<String>) {
        userRepo.replaceFav(favs)
    }

    fun unitDetails(unitId: Int): Observable<Unit> = unitRepo.unitDetails(unitId)
}