package com.example.ezproject.ui.activity.auth.login

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.User
import com.example.ezproject.data.network.response.LoginResponse
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class LoginViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun userLogin(email: String, password: String, device_token: String): Observable<LoginResponse> {
        return userRepo.userLogin(email, password,device_token)
    }

    fun saveUserData(user: User) {
        userRepo.saveUserData(user)
    }

    fun socialSignIn(token: String, type: String): Observable<LoginResponse> {
        return userRepo.socialSignIn(token, type)
    }
}