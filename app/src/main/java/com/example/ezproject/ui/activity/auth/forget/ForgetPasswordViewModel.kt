package com.example.ezproject.ui.activity.auth.forget

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.network.response.BaseResponse
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class ForgetPasswordViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun forgetPassword(email: String): Observable<BaseResponse> {
        return userRepo.forgetPassword(email)
    }
}