package com.example.ezproject.ui.fragment.unit.host

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ezproject.data.network.response.ReviewsResponse
import com.example.ezproject.data.network.response.UnitsResponse
import com.example.ezproject.data.network.response.UserResponse
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class HostProfileViewModel(private val userRepo: UserRepo, private val unitRepo: UnitRepo) :
    ViewModel() {
    val currentCurrency by lazy {
        userRepo.currentCurrency().toUpperCase()
    }
    var hostId: Int? = null
    fun remoteUser(): Observable<UserResponse> {
        return hostId?.let {
            userRepo.remoteUserData(it)
        } ?: Observable.error(IllegalArgumentException("set host id"))
    }

    fun hostUnits(): Observable<UnitsResponse> {
        return hostId?.let {
            unitRepo.userUnits(it)
        } ?: Observable.error(IllegalArgumentException("set host id"))
    }

    fun userReviews(): Observable<ReviewsResponse> {
        return hostId?.let {

            unitRepo.userReviews(it)
        } ?: Observable.error(IllegalArgumentException("set host id"))
    }
}