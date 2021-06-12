package com.example.ezproject.ui.fragment.unit.reviews

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.network.response.ReviewsResponse
import com.example.ezproject.data.repo.UnitRepo
import io.reactivex.Observable

class UnitReviewsViewModel(private val unitRepo: UnitRepo) : ViewModel() {
    var unitId: Int? = null


    fun unitReviews(): Observable<ReviewsResponse> {
        return unitId?.let {
            unitRepo.unitReviews(it)
        } ?: Observable.error(IllegalArgumentException("You must set unit id"))
    }

}