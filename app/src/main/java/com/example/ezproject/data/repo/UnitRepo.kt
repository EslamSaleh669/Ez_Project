package com.example.ezproject.data.repo

import com.example.ezproject.data.models.*
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.network.ApiClient
import com.example.ezproject.data.network.response.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UnitRepo @Inject constructor(
    private val apiClient: ApiClient,
    private val userRepo: UserRepo
) {

    fun currentCurrency(): String {
        return userRepo.currentCurrency()
    }

    fun categories(page: Int, size: Int = 10): Observable<CategoryReponse> =
        apiClient.categories(
            userRepo.currentLang(),
            userRepo.currentCurrency()
            , page).subscribeOn(
            Schedulers.io()
        )

    fun categories_hotels(page: Int, size: Int = 10): Observable<CategoryReponse> =
        apiClient.categories_hotels(
            userRepo.currentLang(),
            userRepo.currentCurrency()
            , page).subscribeOn(
            Schedulers.io()
        )

    fun units(page: Int): Observable<UnitsResponse> =
        apiClient.units(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            page = page
        ).subscribeOn(Schedulers.io())

    fun filteredUnits(filterOptions: FilterOptions,page: Int): Observable<UnitsResponse> {
        return apiClient.filteredUnits(
            userRepo.currentLang(), userRepo.currentCurrency(),
            page,
            filterOptions.originId,
            filterOptions.orderby,
            filterOptions.checkIn,
            filterOptions.checkOut,
            filterOptions.rest,
            filterOptions.prices,
            filterOptions.category,
            filterOptions.amenities,
            filterOptions.guestsCount,
            filterOptions.roomsCount,
            filterOptions.bedsCount,
            filterOptions.bathCount,
            filterOptions.childCount
        ).subscribeOn(Schedulers.io())
    }

    fun AllfilteredHotelss(filterOptions: FilterOptions,page: Int): Observable<AllCorporatesResponse> {
        return apiClient.GetAllfilteredHotels(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            page,10,filterOptions.orderby,
            filterOptions.prices,filterOptions.bedsCount,filterOptions.bathCount,
            filterOptions.guestsCount,filterOptions.childCount,filterOptions.roomsCount,filterOptions.checkIn,filterOptions.checkOut,
            filterOptions.rest,filterOptions.category,filterOptions.amenities,filterOptions.owner_id


        ).subscribeOn(Schedulers.io())
    }

    fun filtereHotels(filterOptions: FilterOptions , page: Int): Observable<HotelResponse> {

        return apiClient.filteredHotels(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                filterOptions.userid!!,
                page

            ).subscribeOn(Schedulers.io())


    }

    fun userUnits(): Observable<UnitsResponse> {
        return userRepo.readUserData()?.let {
            apiClient.filteredUnits(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                1,
                userId = it.id,
                statusIn = arrayListOf("0", "1", "2")
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun deleteUdraft(id:Int): Observable<DeleteDraftResponse> {

        return userRepo.readUserData()?.let {
            apiClient.deleteUnitDraft(
                 "Bearer ${userRepo.readUserData()!!.token}",
                   id
            ).subscribeOn(Schedulers.io())

        }?: Observable.error(IllegalArgumentException())

    }





    fun featuredUnits(page: Int): Observable<UnitsResponse> {
        return apiClient.units(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            featured = 1,
            page = page
        ).subscribeOn(Schedulers.io())
    }

    fun userUnits(userId: Int): Observable<UnitsResponse> =
        apiClient.userUnits(userRepo.currentLang(), userRepo.currentCurrency(), userId).subscribeOn(
            Schedulers.io()
        )

    fun recommendedUnits(page: Int): Observable<UnitsResponse> {
        return userRepo.readUserData()?.let {
            apiClient.units(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                recommended = it.id,
                page = page
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }

    fun topExperianced(page: Int): Observable<UnitsResponse> {
        return apiClient.units(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            orderBy = "RATED",
            page = page
        ).subscribeOn(Schedulers.io())
    }

    fun unitDetails(unitId: Int): Observable<Unit> =
        apiClient.unitDetails(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            unitId
        ).subscribeOn(Schedulers.io()).map {
            it.response.units[0]
        }

    fun completeUnitDetails(unitId: Int): Observable<UnitDetails> =
        apiClient.completeUnitDetails(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            unitId
        ).subscribeOn(Schedulers.io())

    fun unitReviews(unitId: Int): Observable<ReviewsResponse> {
        return userRepo.readUserData()?.let {
            apiClient.uniteReviews(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                unitId = unitId
            )
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }

    fun userReviews(userId: Int): Observable<ReviewsResponse> {
        return userRepo.readUserData()?.let {
            apiClient.uniteReviews(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                userId = userId
            )
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }

    fun places(page: Int, type: String): Observable<TouristPlacesResponse> =
        apiClient.posts(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            page = page,
            type = type
        ).subscribeOn(Schedulers.io())


    fun Hotelplaces(): Observable<List<HotelDesResponse>> =
        apiClient.hoteldestinatons(
            userRepo.currentLang(),
            userRepo.currentCurrency()
        ).subscribeOn(Schedulers.io())

    fun checkUnitAvailable(
        unitId: Int,
        startDate: String,
        endDate: String
    ): Observable<UnitAvailability2> =
        apiClient.checkUnitAvailable(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            "Bearer ${userRepo.readUserData()!!.token}",
            unitId,
            startDate,
            endDate
        ).subscribeOn(Schedulers.io())

    fun checkUnitAvailablefees(
        feeslist: FeesListRequest

    ): Observable<UnitAvailability2> =
        apiClient.checkUnitAvailablebyfees(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            "Bearer ${userRepo.readUserData()!!.token}",
            feeslist

        ).subscribeOn(Schedulers.io())

    fun filterOptions(type: String): Observable<FilterOptionsResponse> =
        apiClient.filterOptions(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            type
        ).subscribeOn(Schedulers.io())

    fun reportUnit(unitId: Int, desc: String): Observable<UnitReportResponse> {
        return userRepo.readUserData()?.let {
            apiClient.reportUnit(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                unitId,
                desc
            ).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }

    fun addUnit2WishList(unitId: Int): Observable<WishlistAddResponse> {
        return userRepo.readUserData()?.let {
            apiClient.add2WishList(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                unitId
            ).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }

    fun myWishList(): Observable<WishListResponse> {
        return userRepo.readUserData()?.let {
            apiClient.myWishList(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}"
            ).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }


    fun searchPlaces(search: String): Observable<List<SearchPlaces>> =
        apiClient.searchPlaces(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            search = search
        ).subscribeOn(Schedulers.io())


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
        return userRepo.readUserData()?.let {
            apiClient.addBooking(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                unitId,
                startDate,
                endDate,
                paymentMethod,
                ownerId,
                adults,
                children,
                price,
                dayPrice,
                fee,
                ezuruFee,
                tourism,
                tax
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }


    fun userBookingsHistory(): Observable<BookingResponse> {
        return userRepo.readUserData()?.let {
            apiClient.userBookings(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                userId = it.id
            )
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }


    fun delete_Booking(bookid:Int): Observable<DeleteDraftResponse> {

        return userRepo.readUserData()?.let {
            apiClient.deleteBooking(

                "Bearer${it.token}" ,
                bookid,
                -2
                ).subscribeOn(Schedulers.io())
        }?: Observable.error(IllegalArgumentException())


    }



    fun userPropertyBookingsHistory(): Observable<BookingResponse> {
        return userRepo.readUserData()?.let {
            apiClient.userBookings(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                ownerId = it.id
            )
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }


    fun cancelPolicy(id: Int): Observable<FilterOptionsResponse> {
        return apiClient.cancelPolicy(id).subscribeOn(Schedulers.io())
    }


    fun unitDraft(id: Int): Observable<UnitDraftResponse> {
        return userRepo.readUserData()?.let {
            apiClient.getOrCreateUnit(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                id
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }


    fun saveUnitDraft(unit: UnitDraftResponse.Unit?): Observable<SaveUnitDraftResponse> {
        return userRepo.readUserData()?.let {
            unit?.method = "put"
            apiClient.saveDraft(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                unit?.id,
                unit

            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun goverments(parentId: String): Observable<GovernmentResponse> {
        return apiClient.location(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            "government",
            parentId
        ).subscribeOn(Schedulers.io())
    }

    fun cities(parentId: String): Observable<GovernmentResponse> {
        return apiClient.location(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            "city",
            parentId
        ).subscribeOn(Schedulers.io())
    }

    fun areas(parentId: String): Observable<GovernmentResponse> {
        return apiClient.location(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            "area",
            parentId
        ).subscribeOn(Schedulers.io())
    }


    fun uploadImage(image: File): Observable<ImageUploadResponse> {
        return apiClient.uploadImage(
            MultipartBody.Part.createFormData(
                "file",
                image.name,
                RequestBody.create("image".toMediaTypeOrNull(), image)
            )
        ).subscribeOn(Schedulers.io())
            ?: Observable.error(IllegalArgumentException())
    }

    fun uploadFile(file: File): Observable<ImageUploadResponse> {
        return apiClient.uploadImage(
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                RequestBody.create("*".toMediaTypeOrNull(), file)
            )
        ).subscribeOn(Schedulers.io())
    }

    fun updateBookingState(bookingId: Int, state: Int): Observable<BaseResponse> {
        return userRepo.readUserData()?.token?.let {
            apiClient.updateBookingStatus(userRepo.currentLang(),userRepo.currentCurrency(),"Bearer $it",bookingId, state)
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun reviewOptions(iamOwner:Boolean): Observable<ReviewOptionsReponse> {
        return apiClient.reviewOptions(if (iamOwner) "review_type_guest" else "review_type").subscribeOn(Schedulers.io())
    }

    fun addReview(userReview: UserReview): Observable<AddReviewResponse> {
        return userRepo.readUserData()?.token?.let {
            apiClient.addReview(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer $it",
                userReview
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun canRateUnit(unitId: Int): Observable<CanReviewResponse> {
        return userRepo.readUserData()?.let {
            apiClient.userCanRateUnit(
                userRepo.currentLang(),
                userRepo.currentCurrency(),
                "Bearer ${it.token}",
                unitId,
                it.id!!
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun payPalInfo(bookingId:Int):Observable<PayPalPayment>{
        return apiClient.payPalPaymentInfo(bookingId).subscribeOn(Schedulers.io())
    }


    fun getCancelationPolicy(title: String): Observable<CancelationPolycyResponse> {
        return userRepo.readUserData().let {
            apiClient.CancelationPolycyRequest(
                 userRepo.currentLang(),
                "application/x-www-form-urlencoded",
                        title

                ).subscribeOn(Schedulers.io())
        }?:Observable.error(IllegalArgumentException())
    }



    fun corporates(page: Int , ccatid:Int): Observable<AllCorporatesResponse> =
        apiClient.allcorporates(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            page = page,
            type = ccatid
        ).subscribeOn(Schedulers.io())


    fun corporatesdes(page:Int ,ccityid:Int): Observable<AllCorporatesResponse> =
        apiClient.allcorporatesbydes(
            userRepo.currentLang(),
            userRepo.currentCurrency(),
            page = page ,
            cityid = ccityid

        ).subscribeOn(Schedulers.io())


}


