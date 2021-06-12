package com.example.ezproject.ui.fragment.chat.chatmessages

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.models.ChatMessage
import com.example.ezproject.data.models.ChatRoom
import com.example.ezproject.ui.adapter.ChatMessagesAdapter
import com.example.ezproject.ui.fragment.chat.chatrooms.ChatViewModel
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.ReplaySubject
import kotlinx.android.synthetic.main.fragment_chat_messages.*
import kotlinx.android.synthetic.main.user_chat_msg_item_layout.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class ChatMessagesFragment : Fragment() {

    @Inject
    @field:Named("chatroom")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChatRoomViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(ChatRoomViewModel::class.java)
    }

    private val autoDispose = AutoDispose()
    private var msgAdapter: ChatMessagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent?.inject(this)
        autoDispose.bindTo(this.lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        val chatRoomId = arguments?.getInt(Constants.CHAT_ROOM_ID_KEY)
        val unitId = arguments?.getInt(Constants.UNIT_ID_KEY)

        msgAdapter = ChatMessagesAdapter(activity, arrayListOf(), viewModel.localUser()?.id!!, null)
        chatMsgRecycler.adapter = msgAdapter
        chatMsgRecycler.layoutManager = LinearLayoutManager(context)
        prepareMsgLoader()

        if (chatRoomId != 0) {
            viewModel.chatRoomId = chatRoomId
            viewModel.loadMessages()

        } else if (unitId != 0) {
            viewModel.ownerId = arguments?.getInt(Constants.OWNER_ID_KEY)
            viewModel.receiver = arguments?.getInt(Constants.RECEIVER_ID_KEY)

            autoDispose.add(
                viewModel.chatRoomsByUnitId(unitId).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.isNotEmpty()) {
                            viewModel.chatRoomId = it[0].id
                            viewModel.ownerId = it[0].ownerId
                            viewModel.receiver = it[0].userId
                            viewModel.unitId = it[0].unitId
                            viewModel.loadMessages()
                        } else {
                            viewModel.unitId = unitId
                            if (viewModel.receiver != 0 && viewModel.ownerId != 0) {
                                autoDispose.add(
                                    viewModel.remoteUserData()
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                            username.text = it.response[0].name

                                        }, {
                                            Timber.e(it)
                                            Log.d("errrrror",it.message)
                                        })
                                )

                            }
                        }
                    }, {
                        Timber.e(it)
                        Log.d("errrrror1",it.message)
                    })
            )
        }
        sendMsgBtn.setOnClickListener {
            val msg: String = chatMsgInput.text.toString().trim()
            if (msg.isNotEmpty()) {
                (chatMsgRecycler.adapter as ChatMessagesAdapter).apply {
                    autoDispose.add(
                        viewModel.sendMsg(msg).observeOn(
                            AndroidSchedulers.mainThread()
                        ).subscribe({

                            if (it.status == Constants.STATUS_OK) {
                                addMsg(
                                    ChatMessage(
                                        "",
                                        -1,
                                        msg,
                                        -1,
                                        "",
                                        "",
                                        viewModel.localUser()!!.id!!,
                                        0
                                    )
                                )

                            }
                        }, {
                            Timber.e(it)
                            context?.handleApiError(it)
                        })
                    )
                    chatMsgRecycler.smoothScrollToPosition(itemCount)
                    chatMsgInput.text.clear()
                }
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            chatMsgRecycler.setOnScrollChangeListener { p0, p1, p2, p3, p4 ->
                val lastPosition: Int =
                    (chatMsgRecycler.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                val currentCount = (chatMsgRecycler.adapter as ChatMessagesAdapter).itemCount
                if (lastPosition == 0 && currentCount != 0) {
                    viewModel.loadMessages()
                }
            }
        }
    }
    // todo user info do not display in first shot
    private fun prepareMsgLoader() {
        autoDispose.add(
            viewModel.chatRoomMessages.observeOn(AndroidSchedulers.mainThread()).subscribe({
                msgAdapter?.otherPartyMember = viewModel.otherPartyMember
                username.text = viewModel.otherPartyMember?.name
                if (msgAdapter?.itemCount ?: 0 > 0) {
                    msgAdapter?.addMsgList(it.chat.messages)
                } else {
                    msgAdapter?.addMsgList(it.chat.messages)
                    chatMsgRecycler.smoothScrollToPosition(msgAdapter?.itemCount ?: 0)
                }
            }, {
                Timber.e(it)
            })
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.compositeDisposable.clear()
        viewModel.page = 1
        viewModel.chatRoomMessages = ReplaySubject.create()
        viewModel.otherPartyMember = null
        viewModel.chatRoomId = 0
        viewModel.unitId = 0
        viewModel.ownerId = 0
        viewModel.chatRoom = null
        viewModel.receiver = 0
    }
}