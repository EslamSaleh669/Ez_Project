package com.example.ezproject.ui.fragment.chat.chatrooms

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.ChatRoom
import com.example.ezproject.data.models.User
import com.example.ezproject.data.network.response.MsgSendResponse
import com.example.ezproject.data.network.response.UserResponse
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable

class ChatViewModel(private val userRepo: UserRepo) : ViewModel() {


    var activeChatRoom: ChatRoom? = null

    fun chatRooms(unitId: Int? = null): Observable<ArrayList<ChatRoom>> =
        userRepo.userChatRooms(unitId)

    fun remoteUserData(userId: Int): Observable<UserResponse> = userRepo.remoteUserData(userId)

    fun sendMsg(msg: String): Observable<MsgSendResponse> {
        return activeChatRoom?.let {
            userRepo.sendMsg(msg, it.unitId, it.ownerId,it.userId)
        } ?: Observable.error<MsgSendResponse>(IllegalArgumentException())
    }




    fun localUser(): User? = userRepo.readUserData()
}