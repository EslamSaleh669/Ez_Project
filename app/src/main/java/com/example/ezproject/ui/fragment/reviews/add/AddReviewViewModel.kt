package com.example.ezproject.ui.fragment.reviews.add

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.UserReview
import com.example.ezproject.data.models.User
import com.example.ezproject.data.network.response.AddReviewResponse
import com.example.ezproject.data.network.response.ReviewOptionsReponse
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class AddReviewViewModel(private val userRepo: UserRepo, private val unitRepo: UnitRepo) :
    ViewModel() {

    fun localUser(): User? {
        return userRepo.readUserData()
    }

    fun reviewOptions(iamOwner: Boolean): Observable<ReviewOptionsReponse> {

        return unitRepo.reviewOptions(iamOwner)
    }

    fun addReview(userReview: UserReview): Observable<AddReviewResponse> {
        return unitRepo.addReview(userReview)
    }
}