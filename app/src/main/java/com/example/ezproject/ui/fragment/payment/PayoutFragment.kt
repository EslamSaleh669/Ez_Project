package com.example.ezproject.ui.fragment.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.PayoutHistoryAdapter
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_pay_out_history.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class PayoutFragment : Fragment() {


    @Inject
    @field:Named("payments")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PaymentsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PaymentsViewModel::class.java)
    }
    private val autoDispose: AutoDispose = AutoDispose()

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
        return inflater.inflate(R.layout.fragment_pay_out_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        autoDispose.add(viewModel.payOuts().observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it.isNotEmpty()) {
                payoutsRecycler.adapter = PayoutHistoryAdapter(it, viewModel.currency())
                payoutsRecycler.layoutManager = LinearLayoutManager(context)
            } else {
                // display no data found


                payoutsRecycler.visibility = View.GONE
                noDataFound.visibility = View.VISIBLE

            }
        }, {
            Timber.e(it)
            context?.handleApiError(it)
        }))

    }

}