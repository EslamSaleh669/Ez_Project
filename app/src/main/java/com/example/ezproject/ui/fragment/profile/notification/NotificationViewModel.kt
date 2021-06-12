package com.example.ezproject.ui.fragment.profile.notification

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.Notification
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class NotificationViewModel(private val userRepo: UserRepo) : ViewModel() {


    fun userNotifications(): Observable<List<Notification>> {
        return userRepo.userNotification()
    }
}