package com.example.ezproject.ui.fragment.unit.reservation

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.FeesListRequest
import com.example.ezproject.data.network.response.AddBookingReponse
import com.example.ezproject.data.repo.UnitRepo
import io.reactivex.Observable

class ReservationViewModel(private val unitRepo: UnitRepo) : ViewModel() {

    val currentCurrency by lazy {
        unitRepo.currentCurrency().toUpperCase()
    }
    fun addBooking(
        unitId: Int,
        ownerId: Int,
        startDate: String,
        endDate: String,
        paymentMethod: String,
        adults: Int,
        children: Int,
        price: Float,
        dayPrice: Float,
        fee: Float,
        ezuruFee: Float,
        tourism: Float,
        tax: Float

    ): Observable<AddBookingReponse> {
        return unitRepo.addBooking(
            unitId,
            ownerId,
            startDate,
            endDate,
            paymentMethod,
            adults,
            children,
            price,
            dayPrice,
            fee,
            ezuruFee,
            tourism,
            tax
        )
    }
}