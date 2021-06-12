package com.example.ezproject.ui.activity.auth.register

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.User
import com.example.ezproject.data.network.response.LoginResponse
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class RegisterViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun userRegister(
        name: String,
        email: String,
        password: String,
        phone: String,
        dev_token: String
    ): Observable<LoginResponse> {
        return userRepo.userRegister(name, email, password, phone,dev_token)
    }

    fun saveUserData(user: User) {
        userRepo.saveUserData(user)
    }
}