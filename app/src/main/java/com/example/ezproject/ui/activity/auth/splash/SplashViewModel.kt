package com.example.ezproject.ui.activity.auth.splash

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.User
import com.example.ezproject.data.repo.UserRepo

class SplashViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun userData(): User? = userRepo.readUserData()
}