package com.example.ezproject.ui.fragment.chat.chatrooms

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.data.models.ChatRoom
import com.example.ezproject.ui.adapter.ChatRoomsAdapter
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.ui.fragment.main.MainFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_chat_rooms.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ChatRoomsFragment : Fragment(), ChatRoomsAdapter.OnChatRoomClickListener {

    @Inject
    @field:Named("chatrooms")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var chatRooms:ArrayList<ChatRoom>? = null
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(activity!!, viewModelFactory).get(ChatViewModel::class.java)
    }

    private val autoDispose = AutoDispose()

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
        return inflater.inflate(R.layout.fragment_chat_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        val userId = viewModel.localUser()?.id!!
        autoDispose.add(viewModel.chatRooms().observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it.isNotEmpty()) {
                noDataLayout.visibility = View.GONE
                chatRoomsRecycler.visibility = View.VISIBLE
                searchInput.visibility = View.VISIBLE
                chatRooms = it
                chatRoomsRecycler.adapter = ChatRoomsAdapter(activity, it, userId,this)
                Log.d("outttts",it.toString())
                chatRoomsRecycler.layoutManager = LinearLayoutManager(context)

                searchInput.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (text?.trim()?.isNotEmpty() == true){
                            chatRooms?.let {
                                (chatRoomsRecycler.adapter as ChatRoomsAdapter).setSearchResult(it.filter {
                                    if (userId != it.ownerId){
                                        it.owner.name!!.toLowerCase().contains(text.toString().toLowerCase())
                                    }else{
                                        it.guest.name!!.toLowerCase().contains(text.toString().toLowerCase())
                                    }
                                })
                            }
                        }else{
                            (chatRoomsRecycler.adapter as ChatRoomsAdapter).setSearchResult(it)
                        }
                    }
                })

            }
        }, {
            Timber.e(it)
            context?.handleApiError(it)
        }))

        exploreBtn.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer,MainFragment())
                addToBackStack("")
            }
        }
    }

    override fun onChatRoomClicked(chatRoom: ChatRoom) {
        viewModel.activeChatRoom = chatRoom
        activity?.supportFragmentManager?.commit {
            replace(R.id.fragmentContainer, ChatMessagesFragment().apply {
                arguments = bundleOf(Pair(Constants.CHAT_ROOM_ID_KEY,chatRoom.id))
            })
            addToBackStack("")
        }
    }
}