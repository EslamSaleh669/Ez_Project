package com.example.ezproject.ui.fragment.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.NotificationAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_notification.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class NotificationFragment : Fragment() {
    @Inject
    @field:Named("notification")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: NotificationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NotificationViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDispose.bindTo(this.lifecycle)
        (activity?.application as MyApplication).appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        autoDispose.add(
            viewModel.userNotifications().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (it.isNotEmpty()) {
                        notificationRecycler.visibility = View.VISIBLE
                        notificationRecycler.adapter = NotificationAdapter(activity, it)
                        notificationRecycler.layoutManager = LinearLayoutManager(context)
                    }else{
                        noData.visibility = View.VISIBLE

                    }
                },
                {
                    Timber.e(it)
                    context?.handleApiError(it)
                })
        )
    }
}