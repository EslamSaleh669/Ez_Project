package com.example.ezproject.ui.fragment.unit.details

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.*
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.network.response.*
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableError
import io.reactivex.schedulers.Schedulers

class UnitDetailsViewModel(private val unitRepo: UnitRepo, private val userRepo: UserRepo) :
    ViewModel() {
    var unitId: Int? = null
    val currentCurrency by lazy {
        userRepo.currentCurrency().toUpperCase()
    }

    fun unitDetails(): Observable<Unit> {
        unitId?.let {
            return unitRepo.unitDetails(it)
        } ?: return ObservableError.error(
            IllegalArgumentException("unit id cannot be null. set unit id first")
        )
    }

    fun completeUnitDetails(): Observable<UnitDetails> {
        unitId?.let {
            return unitRepo.completeUnitDetails(it)
        } ?: return ObservableError.error(
            IllegalArgumentException("unit id cannot be null. set unit id first")
        )
    }

    // todo units in the same area id
    fun similarUnits(): Observable<UnitsResponse> {
        unitId?.let {
            return unitRepo.units(1)
        } ?: return ObservableError.error(
            IllegalArgumentException("unit id cannot be null. set unit id first")
        )
    }

    fun isInFav(): Boolean {
        return unitId?.let {
            userRepo.isInFav(it)
        } ?: false
    }

    fun singleReview(): Observable<ReviewsResponse> {
        unitId?.let {
            return unitRepo.unitReviews(it)
        } ?: return ObservableError.error(
            IllegalArgumentException("unit id cannot be null. set unit id first")
        )
    }

    fun checkUnitAvailable(
        unitId: Int,
        startDate: String,
        endDate: String
    ): Observable<UnitAvailability2> =
        unitRepo.checkUnitAvailable(unitId, startDate, endDate)



    fun checkUnitAvailablefee(
        feeslist: FeesListRequest
    ): Observable<UnitAvailability2> =
        unitRepo.checkUnitAvailablefees(feeslist)



    fun hostData(hostId: Int): Observable<UserResponse> {
        return userRepo.remoteUserData(hostId)
    }

    fun reportUnit(desc: String): Observable<UnitReportResponse> {
        unitId?.let {
            return unitRepo.reportUnit(it, desc)
        } ?: return ObservableError.error(
            IllegalArgumentException("unit id cannot be null. set unit id first")
        )
    }

    fun addUnit2WishList(): Observable<WishlistAddResponse> {
        unitId?.let {
            return unitRepo.addUnit2WishList(it)
        } ?: return ObservableError.error(
            IllegalArgumentException("unit id cannot be null. set unit id first")
        )
    }

    fun addUnit2LocalWishList() {
        unitId?.let {
            userRepo.addItem2Fav(it)
        }
    }

    fun cancelPolicy(id: Int): Observable<FilterOptionsResponse> {
        return unitRepo.cancelPolicy(id)
    }

    fun gettheCancelationPolicy(title :String): Observable<CancelationPolycyResponse> {
        return unitRepo.getCancelationPolicy(title)
    }


    fun loadAllCorporates(page:Int,catid:Int): Observable<AllCorporatesResponse> {

            return unitRepo.corporates(page,catid)

    }

    fun loadAllCorporatesbydes(page:Int ,cityid:Int): Observable<AllCorporatesResponse> {

            return unitRepo.corporatesdes(page ,cityid)

    }
}