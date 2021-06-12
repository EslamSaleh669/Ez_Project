package com.example.ezproject.ui.fragment.profile

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.*
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.network.response.*
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable
import java.io.File
import com.example.ezproject.data.models.User


class ProfileViewModel(private val userRepo: UserRepo, private val unitRepo: UnitRepo) :
    ViewModel() {

    val currency = userRepo.currentCurrency()
    val language = userRepo.currentLang()
    var selectedBooking: Booking? = null
    fun currentUser(): User? {
        return userRepo.readUserData()
    }

    fun logout() {
        userRepo.clearUserData()
    }

    fun saveUserData(user: User) {
        userRepo.saveUserData(user)
    }

    fun userOnline(): Observable<User> {
        return userRepo.fetchCurrentUserDataRemotely()
    }

    fun updateUserPassword(password: String): Observable<UserUpdateResponse> {
        return userRepo.updateUserPassword(password)
    }

    fun updateUser(
        fName: String,
        email: String,
        phone: String,
        desc: String,
        location: String,
        userImg: File?
    ): Observable<UserUpdateResponse> {
        return currentUser()?.let {
            it.name = fName
            it.email = email
            it.mobile = phone
            it.description = desc
            it.city = location
            it.imageFile = userImg
            userRepo.updateUserData(it)
        } ?: Observable.error(IllegalArgumentException())
    }

    fun userUnits(): Observable<UnitsResponse> {
        return unitRepo.userUnits()
    }

    fun userBooking(): Observable<BookingResponse> {
        return unitRepo.userBookingsHistory()
    }

    fun deletebooking(id :Int): Observable<DeleteDraftResponse> {
        return unitRepo.delete_Booking(id)
    }

    fun deleteundraft(id :Int): Observable<DeleteDraftResponse> {
        return unitRepo.deleteUdraft(id)
    }

    fun userPropertyBooking(): Observable<BookingResponse> {
        return unitRepo.userPropertyBookingsHistory()
    }

    fun unitDetails(unitId: Int): Observable<Unit> {
        return unitRepo.unitDetails(unitId)
    }

    fun verifyEmail(): Observable<VerifyEmailResponse> {
        return userRepo.verifyEmail()
    }

    fun verifyPhone(mobile:String): Observable<String> {
        return userRepo.verifyPhone(mobile)
    }

    fun validatePhone(code: String): Observable<String> {
        return userRepo.validatePhone(code)
    }

    fun updateBookingState(booking: Booking): Observable<BaseResponse> {
        return unitRepo.updateBookingState(booking.id, booking.status)
    }


    fun canReviewUnit(unitid: Int): Observable<CanReviewResponse> {
        return unitRepo.canRateUnit(unitid)
    }

    fun userDetails(userId: Int): Observable<UserResponse> {
        return userRepo.remoteUserData(userId)
    }

    fun uploadNatIdImg(image: File): Observable<CurrentUserResponse> {
        return userRepo.uploadNatIdImg(image)
    }

    fun loadPayPalInfo(bookingId: Int): Observable<PayPalPayment> {
        return unitRepo.payPalInfo(bookingId)
    }
}