package com.example.ezproject.ui.fragment.payment

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.PayIn
import com.example.ezproject.data.models.Payout
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class PaymentsViewModel(private val userRepo: UserRepo) : ViewModel() {


    fun payIns(): Observable<List<PayIn>> {
        return userRepo.getPayIns()
    }


    fun payOuts(): Observable<List<Payout>> {
        return userRepo.getPayouts()
    }

    fun currency(): String = userRepo.currentCurrency()
}