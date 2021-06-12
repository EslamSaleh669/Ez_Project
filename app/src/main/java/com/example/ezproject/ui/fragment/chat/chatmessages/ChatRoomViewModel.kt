package com.example.ezproject.ui.fragment.chat.chatmessages

import androidx.lifecycle.ViewModel
import com.example.ezproject.data.models.ChatRoom
import com.example.ezproject.data.models.User
import com.example.ezproject.data.network.response.ChatRoomMessagesResponse
import com.example.ezproject.data.network.response.MsgSendResponse
import com.example.ezproject.data.network.response.UserResponse
import com.example.ezproject.data.repo.UserRepo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject


class ChatRoomViewModel(private val userRepo: UserRepo) : ViewModel() {
    var compositeDisposable = CompositeDisposable()
    var chatRoom: ChatRoomMessagesResponse.Message? = null
    var page = 1
    var chatRoomMessages: ReplaySubject<ChatRoomMessagesResponse> = ReplaySubject.create()
    var otherPartyMember: User? = null

    var chatRoomId: Int? = null

    var unitId: Int? = null
    var ownerId: Int? = null
    var receiver: Int? = null

    fun remoteUserData(): Observable<UserResponse> {
        return localUser()?.let {
            userRepo.remoteUserData(if (ownerId ?: 0 == it.id) receiver!! else ownerId!!)
        } ?: Observable.error(IllegalArgumentException())

    }

    fun sendMsg(msg: String): Observable<MsgSendResponse> {
        return chatRoom?.let {
            userRepo.sendMsg(msg, it.unitId, it.ownerId, it.userId)
        } ?: run {
            if (unitId != 0 && ownerId != 0 && receiver != 0) {
                userRepo.sendMsg(msg, unitId!!, ownerId!!, receiver!!)

            } else {
                Observable.error(IllegalArgumentException())

            }
        }
    }

    fun loadMessages() {
        chatRoomId?.let {
            compositeDisposable.add(userRepo.userChatRoomMessage(it, page).subscribe({
                if (otherPartyMember == null) {
                    localUser()?.let { user ->
                        otherPartyMember = if (user.id == it.message.userId) {
                            it.message.owner
                        } else {
                            it.message.guest
                        }
                    }
                }

                if (it.chat.messages.isNotEmpty()) {
                    page++
                    chatRoomMessages.onNext(it)
                }
                if (chatRoom == null) {
                    chatRoom = it.message
                }
            }, {
                chatRoomMessages.onError(it)
             }))
        } ?: chatRoomMessages.onError(IllegalArgumentException("Set chat id first")

        )
    }

    fun chatRoomsByUnitId(unitId: Int? = null): Observable<ArrayList<ChatRoom>> =
        userRepo.userChatRooms(unitId, receiver)

    fun localUser(): User? = userRepo.readUserData()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}