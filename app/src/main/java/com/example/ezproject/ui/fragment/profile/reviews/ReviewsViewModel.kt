package com.example.ezproject.ui.fragment.profile.reviews

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.network.response.UserReviewsResponse
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject

class ReviewsViewModel(private val userRepo: UserRepo) : ViewModel() {
    val reviews: ReplaySubject<ArrayList<UserReviewsResponse.Response.Review>> =
        ReplaySubject.create()
    val compositeDisposable = CompositeDisposable()
    var page: Int = 1
    var currentLimit = 9
    var iamOwner: Boolean = false
    fun loadReviews() {
        compositeDisposable.add(userRepo.userReviews(iamOwner, page).subscribe({
            page++
            currentLimit += 10
            reviews.onNext(it)
        }, {
            reviews.onError(it)
        }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun checkForLoadingData(lastPosition: Int) {
        if (lastPosition == currentLimit) {
            loadReviews()
        }
    }
}