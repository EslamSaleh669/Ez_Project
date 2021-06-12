package com.example.ezproject.data.network

import com.example.ezproject.data.models.*
import com.example.ezproject.data.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiClient {


    @POST("v1/users/login")
    fun userLogin(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("device_token") devicetoken: String

    ): Observable<LoginResponse>

    @POST("v1/users")
    fun userRegister(
        @Query("user[name]") name: String,
        @Query("user[password]") password: String,
        @Query("user[email]") email: String,
        @Query("user[mobile]") phone: String,
        @Query("device_token") device_token: String
    ): Observable<LoginResponse>


    @POST("v1/users/forget")
    @FormUrlEncoded
    fun forgetPassword(@Field("email") email: String): Observable<BaseResponse>

    @GET("v1/taxonomy")
    fun categories(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("limit") size: Int = 10,
        @Query("type") type: String = "category"
    ): Observable<CategoryReponse>

    @GET("v1/taxonomy")
    fun categories_hotels(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("limit") size: Int = 10,
        @Query("type") type: String = "category"
    ): Observable<CategoryReponse>


    @GET("v1/units")
    fun units(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("featured") featured: Int? = null,
        @Query("recomended") recommended: Int? = null,
        @Query("OrderBy") orderBy: String? = null,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int

    ): Observable<UnitsResponse>

    @GET("v1/units")
    fun filteredUnits(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("origin_id") originId: Int? = null,
        @Query("OrderBy") orderBy: String? = null,
        @Query("check_in") checkInDate: String? = null,
        @Query("check_out") checkOutDate: String? = null,
        @Query("Rest[]") rest: ArrayList<Int>? = null,
        @Query("price_in[]") prices: ArrayList<Float>? = null,
        @Query("Types[]") categories: ArrayList<Int>? = null,
        @Query("Amenities[]") amenities: ArrayList<Int>? = null,
        @Query("Units['guests']") guestsCount: String? = null,
        @Query("Units['rooms']") rooms: String? = null,
        @Query("Units['beds']") beds: String? = null,
        @Query("Units['bathrooms']") bath: String? = null,
        @Query("Units['max_children']") children: String? = null,
        @Query("user_id") userId: Int? = null,
        @Query("StatusIn[]") statusIn: List<String>? = null,
        @Query("limit") limit: Int? = 10
    ): Observable<UnitsResponse>

    @GET("v1/hotels")
    fun GetAllfilteredHotels(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int? = 10,
        @Query("OrderBy") orderBy: String? = null,
        @Query("price_in[]") prices: ArrayList<Float>? = null,
        @Query("beds") beds: String? = null,
        @Query("bathrooms") bath: String? = null,
        @Query("guests") guestsCount: String? = null,
        @Query("max_childrens") children: String? = null,
        @Query("rooms") rooms: String? = null,
        @Query("start_date") checkInDate: String? = null,
        @Query("end_date") checkOutDate: String? = null,
        @Query("Rest[]") rest: ArrayList<Int>? = null,
        @Query("Types[]") categories: ArrayList<Int>? = null,
        @Query("Amenities[]") amenities: ArrayList<Int>? = null,
        @Query("owner_id") ownerId: Int? = null
        //@Query("city") originId: Int? = null,


    ): Observable<AllCorporatesResponse>
//
//    start_date=2019-12-15&end_date=2030-12-19&title&rooms&price_in[0]&price_in[1]&beds&bathrooms&guests&max_childrens&Types[]=&Amenities[]&Rest[]&city&not_in
//

    @GET("v1/hotel/{user_id}")
    fun filteredHotels(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Path("user_id") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int? = 10
    ): Observable<HotelResponse>

    @GET("v1/units")
    fun userUnits(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("user") userId: Int
    ): Observable<UnitsResponse>

    @GET("v1/units")
    fun unitDetails(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("id") unitId: Int
    ): Observable<UnitsResponse>

    @GET("cleaning/units/{id}")
    fun deleteUnitDraft(
//        @Header("Accept-Language") lang: String,
//        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Path("id") unitId: Int
    ): Observable<DeleteDraftResponse>



    @GET("v1/units/{id}")
    fun completeUnitDetails(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Path("id") unitId: Int
    ): Observable<UnitDetails>



    @GET("v1/posts")
    fun posts(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("limit") size: Int = 10,
        @Query("type") type: String? = null
    ): Observable<TouristPlacesResponse>

    @POST("v1/is-booking-available")
    @FormUrlEncoded
    fun  checkUnitAvailable(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Field("unit_id") unitId: Int,
        @Field("date_start") startDate: String,
        @Field("date_end") endDate: String
    ): Observable<UnitAvailability2>


    @POST("v1/is-booking-available")
    fun  checkUnitAvailablebyfees(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Body data: FeesListRequest
    ): Observable<UnitAvailability2>

    @GET("v1/taxonomy")
    fun filterOptions(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("type") type: String
    ): Observable<FilterOptionsResponse>

    @GET("v1/taxonomy")
    fun cancelPolicy(@Query("id") type: Int): Observable<FilterOptionsResponse>

    @GET("v1/reviews")
    fun uniteReviews(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("status") status: Int = 1,
        @Query("owner_id") userId: Int? = null,
        @Query("unit_id") unitId: Int? = null
    ): Observable<ReviewsResponse>

    @GET("v1/users")
    fun getUserData(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("id") userId: Int
    ): Observable<UserResponse>

    @POST("v1/flag-unit")
    @FormUrlEncoded
    fun reportUnit(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String, @Field("flagged_id") unitId: Int, @Field(
            "description"
        ) desc: String
    ): Observable<UnitReportResponse>


    @POST("v1/add-unit-to-wishlist")
    fun add2WishList(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Query("unit_id") unitId: Int
    ): Observable<WishlistAddResponse>

    @GET("v1/my-wishlist")
    fun myWishList(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String
    ): Observable<WishListResponse>

    @GET("front/filterCity")
    fun searchPlaces(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("type") type: String = "style2",
        @Query("search") search: String,
        @Query("locale") local: String = "en"
    ): Observable<List<SearchPlaces>>

    @POST("v1/messages")
    @FormUrlEncoded
    fun sendMsg(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Field("unit_id") unitId: Int,
        @Field("message") msg: String,
        @Field("owner_id") ownerId: Int,
        @Field("user_id") userId: Int
    ): Observable<MsgSendResponse>

    @GET("v1/messages")
    fun chatRooms(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Query("unit_id") unitId: Int? = null,
        @Query("user_id") userId: Int? = null
    ): Observable<ChatRoomsResponse>


    @GET("v1/messages/{id}")
    fun chatRoomMessages(
        @Path("id") chatRoomId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int? = 50,
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String
    ): Observable<ChatRoomMessagesResponse>

    @GET("v1/my-notifications")
    fun notification(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String
    ): Observable<List<Notification>>

    @POST("v1/users/{userId}")
    @Multipart
    fun updateUser(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Path("userId") userId: Int,
        @Header("Authorization") token: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("description") description: String,
        @Query("password") password: String? = null,
        @Query("_method") method: String = "PUT",
        @Part avatar: MultipartBody.Part?,
        @Part("dummy") dummy: String? = "dummy"
    ): Observable<UserUpdateResponse>


    @POST("v1/add-booking")
    @FormUrlEncoded
    fun addBooking(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Field("unit_id") unitId: Int,
        @Field("date_start") startDate: String,
        @Field("date_end") endDate: String,
        @Field("PaymentMethod") PaymentMethod: String,
        @Field("owner_id") ownerId: Int,
        @Field("adults") adults: Int,
        @Field("childrens") children: Int,
        @Field("price") price: Float,
        @Field("day_price") dayPrice: Float,
        @Field("fee") fee: Float,
        @Field("ezuru_fee") ezuruFee: Float,
        @Field("tourism") tourism: Float,
        @Field("tax") tax: Float
//        @Field("fees") fees: FeesList
 //       @Field("fees[]") fees: ArrayList<FeesItem>
//
        //  @Body items : ArrayList<JSONObject>

    ): Observable<AddBookingReponse>


    @GET("v1/unit-bookings")
    fun userBookings(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int? = null,
        @Query("owner_id") ownerId: Int? = null
    ): Observable<BookingResponse>
//    @POST("v1/add-review")
//    fun addReview()


    @POST("v1/booking/cancel")
    fun deleteBooking(
        @Header("Authorization") token: String,
        @Query("BookingId") bookingId: Int? = null,
        @Query("Status") status: Int? = null
    ): Observable<DeleteDraftResponse>



    @GET("front/new/units/{id}")
    fun getOrCreateUnit(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Observable<UnitDraftResponse>


    @POST("units/{id}")
    fun saveDraft(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Path("id") id: String?,
        @Body draft: UnitDraftResponse.Unit?
    ): Observable<SaveUnitDraftResponse>


    @GET("v1/taxonomy")
    fun location(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("type") type: String,
        @Query("parent") parent: String
    ): Observable<GovernmentResponse>


    @Multipart
    @POST("front/upload")
    fun uploadImage(@Part image: MultipartBody.Part): Observable<ImageUploadResponse>

    @FormUrlEncoded
    @POST("v1/users/authViaSocial")
    fun socialSignIn(
        @Field("token") token: String,
        @Field("type") type: String
    ): Observable<LoginResponse>

    @GET("v1/listpayments")
    fun getPayIn(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("currency") currencyQ: String,
        @Header("Authorization") token: String
    ): Observable<List<PayIn>>

    @GET("v1/listpayouts")
    fun getPayout(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("currency") currencyQ: String,
        @Header("Authorization") token: String
    ): Observable<List<Payout>>

    @GET("v1/whoami")
    fun currentUser(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String
    ): Observable<CurrentUserResponse>


    @POST("v1/users/verify")
    @FormUrlEncoded
    fun sendVerifyEmail(
        @Header("Authorization") tokenH: String,
        @Field("token") token: String
    ): Observable<VerifyEmailResponse>

    @GET("front/mobile/confirm")
    fun verifyPhone(
        @Header("Authorization") tokenH: String ,
        @Query("mobile") type: String
        ): Observable<String>

    @GET("front/mobile/validate/{code}")
    fun validatePhone(
        @Header("Authorization") tokenH: String,
        @Path("code") code: String
    ): Observable<String>

    @POST("v1/set-booking-status")
    fun updateBookingStatus(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Query("BookingId") bookingId: Int,
        @Query("Status") state: Int
    ): Observable<BaseResponse>

    //
    @GET("v1/taxonomy?page=1&limit=100")
    fun reviewOptions(@Query("type") type: String): Observable<ReviewOptionsReponse>

    @POST("v1/add-review")
    fun addReview(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Body review: UserReview
    ): Observable<AddReviewResponse>


    @GET("v1/can-review-unit")
    fun userCanRateUnit(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Query("unit_id") unitId: Int,
        @Query("guest_id") guestId: Int
    ): Observable<CanReviewResponse>


    @GET("v1/reviews")
    fun userReviews(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Header("Authorization") token: String,
        @Query("owner_id") ownerId: Int?,
        @Query("guest_id") guestId: Int?,
        @Query("page") page: Int,
        @Query("status") status: Int? = 1,
        @Query("limit") limit: Int? = 10
    ): Observable<UserReviewsResponse>

    @POST("v1/users/update-id-photo")
    @Multipart
    fun uploadPhotoId(
        @Header("Authorization") token: String,
        @Part idImage: MultipartBody.Part?,
        @Part("dummy") dummy: String? = "dummy"
    ): Observable<CurrentUserResponse>


    @GET("payment/create/{id}")
    fun payPalPaymentInfo(@Path("id") bookingId: Int): Observable<PayPalPayment>

    @POST("static/cancle")
    @FormUrlEncoded
    fun CancelationPolycyRequest(
        @Header("language") lang: String,
        @Header("Accept") accept: String,
        @Field("title_en") title: String

    ): Observable<CancelationPolycyResponse>
////


    @GET("v1/hotels")
    fun allcorporates(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("Types[]") type: Int,
        @Query("limit") size: Int = 10
    ): Observable<AllCorporatesResponse>

    @GET("v1/hotels")
    fun allcorporatesbydes(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("page") page: Int,
        @Query("limit") size: Int = 10,
        @Query("city") cityid: Int
    ): Observable<AllCorporatesResponse>


    @GET("front/filterCity")
    fun hoteldestinatons(
        @Header("Accept-Language") lang: String,
        @Header("Accept-Currency") currency: String,
        @Query("all") type: Int=1
    ): Observable<List<HotelDesResponse>>


}