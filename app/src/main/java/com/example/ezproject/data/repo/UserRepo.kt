package com.example.ezproject.data.repo

import android.content.SharedPreferences
import com.example.ezproject.data.models.*
import com.example.ezproject.data.network.ApiClient
import com.example.ezproject.data.network.response.*
import com.example.ezproject.util.Constants
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import com.example.ezproject.data.models.User



class UserRepo @Inject constructor(
    private val apiClient: ApiClient,
    private val shared: SharedPreferences
) {

    fun userLogin(email: String, password: String , devtoken:String): Observable<LoginResponse> {
        return apiClient.userLogin(email, password,devtoken ).subscribeOn(Schedulers.io())
    }

    fun saveUserData(user: User) {
        shared.edit().putString(Constants.USER_KEY, Gson().toJson(user)).apply()
    }

    fun readUserData(): User? {
        return try {
            Gson().fromJson(shared.getString(Constants.USER_KEY, ""), User::class.java)
        } catch (e: Exception) {
            null
        }
    }


    fun userRegister(
        name: String,
        email: String,
        password: String,
        phone: String,
        devicetoken: String
    ): Observable<LoginResponse> {
        return apiClient.userRegister(name, password, email, phone,devicetoken).subscribeOn(Schedulers.io())
    }

    fun forgetPassword(email: String): Observable<BaseResponse> {
        return apiClient.forgetPassword(email).subscribeOn(Schedulers.io())
    }

    fun currentLang(): String {
        return shared.getString(Constants.LANG_KEY, "en") ?: "en"
    }

    fun currentCurrency(): String {
        return shared.getString(Constants.CURRENCY_KEY, "egp") ?: "egp"
    }

    fun userChatRooms(unitId: Int? = null,userId: Int? = null): Observable<ArrayList<ChatRoom>> {
        return readUserData()?.let {
            apiClient.chatRooms(currentLang(), currentCurrency(), "Bearer ${it.token}", unitId,userId)
                .subscribeOn(Schedulers.io()).map {
                    it.chatRooms
                }
        } ?: Observable.error(IllegalArgumentException())
    }


    fun userChatRoomMessage(chatRoomId: Int, page: Int): Observable<ChatRoomMessagesResponse> {
        return readUserData()?.let {
            apiClient.chatRoomMessages(
                chatRoomId,
                page,
                50,
                currentLang(),
                currentCurrency(),
                "Bearer ${it.token}"
            )
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun sendMsg(msg: String, unitId: Int, ownerId: Int, userId: Int): Observable<MsgSendResponse> {
        return readUserData()?.let {
            apiClient.sendMsg(
                currentLang(),
                currentCurrency(),
                "Bearer ${it.token}",
                unitId,
                msg,
                ownerId,
                userId
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun clearUserData() {
        shared.edit().clear().apply()
    }

    fun remoteUserData(userId: Int): Observable<UserResponse> =
        apiClient.getUserData(currentLang(), currentCurrency(), userId).subscribeOn(Schedulers.io())

    fun userNotification(): Observable<List<Notification>> {
        return readUserData()?.let {
            apiClient.notification(currentLang(), currentCurrency(), "Bearer ${it.token}")
                .subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }

    fun updateUserData(user: User): Observable<UserUpdateResponse> {
        return readUserData()?.let {
            apiClient.updateUser(
                currentLang(), currentCurrency(),
                it.id!!,
                "Bearer ${it.token}",
                user.name!!,
                user.email!!,
                user.mobile!!,
                user.description!!, null, "put",
                user.imageFile?.let {
                    MultipartBody.Part.createFormData(
                        "avatar",
                        user.imageFile?.name,
                        RequestBody.create("image".toMediaTypeOrNull(), it)
                    )
                }

            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }


    fun updateUserPassword(password: String): Observable<UserUpdateResponse> {
        return readUserData()?.let {
            apiClient.updateUser(
                currentLang(), currentCurrency(),
                it.id!!,
                "Bearer ${it.token}",
                it.name!!,
                it.email!!,
                it.mobile!!,
                it.description!!,
                password, "put", null
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())
    }


    fun addItem2Fav(unitId: Int): Boolean {
        val favs = shared.getStringSet(Constants.FAV_KEY, mutableSetOf()) ?: mutableSetOf()
        return if (favs.contains(unitId.toString())) {
            favs.remove(unitId.toString())
            shared.edit().remove(Constants.FAV_KEY).apply()
            shared.edit().putStringSet(Constants.FAV_KEY, favs)

            false
        } else {
            favs.add(unitId.toString())
            shared.edit().remove(Constants.FAV_KEY).apply()
            shared.edit().putStringSet(Constants.FAV_KEY, favs).apply()
            true
        }
    }

    fun isInFav(unitId: Int): Boolean {
        return shared.getStringSet(Constants.FAV_KEY, mutableSetOf())?.contains(unitId.toString())
            ?: false
    }

    fun replaceFav(favs: MutableSet<String>) {
        shared.edit().remove(Constants.FAV_KEY).apply()
        shared.edit().putStringSet(Constants.FAV_KEY, favs).apply()

    }


    fun socialSignIn(token: String, type: String): Observable<LoginResponse> {
        return apiClient.socialSignIn(token, type).subscribeOn(Schedulers.io())
    }


    fun getPayIns(): Observable<List<PayIn>> {
        return readUserData()?.let {
            apiClient.getPayIn(
                currentLang(),
                currentCurrency(),
                currentCurrency(),
                "Bearer ${it.token}"
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }


    fun getPayouts(): Observable<List<Payout>> {
        return readUserData()?.let {
            apiClient.getPayout(
                currentLang(),
                currentCurrency(),
                currentCurrency(),
                "Bearer ${it.token}"
            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }


    fun fetchCurrentUserDataRemotely(): Observable<User> {
        return readUserData()?.token?.let { token ->
            apiClient.currentUser(currentLang(), currentCurrency(), "Bearer ${token}")
                .map {
                    it.user.token = token

                    saveUserData(it.user)
                    it.user
                }.subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException())

    }

    fun verifyEmail(): Observable<VerifyEmailResponse> {
        return readUserData()?.let {
            apiClient.sendVerifyEmail("Bearer ${it.token}", it.token!!).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException("Missing user email"))
    }

    fun verifyPhone(mob:String): Observable<String> {
        return readUserData()?.let {
            apiClient.verifyPhone("Bearer ${it.token}",mob).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException("Missing user email"))
    }

    fun validatePhone(code: String): Observable<String> {
        return readUserData()?.let {
            apiClient.validatePhone("Bearer ${it.token}", code).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException("Missing user email"))
    }

    fun userReviews(
        iamOwner: Boolean,
        page: Int
    ): Observable<ArrayList<UserReviewsResponse.Response.Review>> {
        return readUserData()?.let {
            apiClient.userReviews(
                currentLang(),
                currentCurrency(), "Bearer ${it.token}",
                if (iamOwner) it.id else null,
                if (!iamOwner) it.id else null,
                page
            ).map { it.response.reviews }.subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException("Missing user "))
    }

    fun uploadNatIdImg(file: File): Observable<CurrentUserResponse> {
        return readUserData()?.let {
            apiClient.uploadPhotoId(
                "Bearer ${it.token}",
                file.let {
                    MultipartBody.Part.createFormData(
                        "photoid",
                        it.name,
                        RequestBody.create("image".toMediaTypeOrNull(), it)
                    )
                }

            ).subscribeOn(Schedulers.io())
        } ?: Observable.error(IllegalArgumentException("Missing user "))
    }

}
/*image = userImg?.let {
                    MultipartBody.Part.createFormData(
                        "avatar", it.name,
                        RequestBody.create(
                            MediaType.parse("image/*"),
                            it
                        )
                    ) ?: null
                }*/