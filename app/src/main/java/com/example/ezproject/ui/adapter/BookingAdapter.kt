package com.example.ezproject.ui.adapter

import android.app.Activity
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Booking
import com.example.ezproject.data.models.PayPalPayment
import com.example.ezproject.data.models.Unit
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.ui.fragment.profile.booking.BookingDetailsFragment
import com.example.ezproject.ui.fragment.reviews.add.AddReviewFragment
import com.example.ezproject.ui.fragment.unit.reservation.ReservationPaymentFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.extensions.makeToast
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class BookingAdapter(
    private val bookings: ArrayList<Booking>,
    private val activity: Activity?,
    private val isMyUnit: Boolean,
    private val autoDispose: AutoDispose,
    private val loadUnit: (Int) -> Observable<Unit>,
    private val updateBookingState: (Booking) -> kotlin.Unit,
    private val onReviewClicked: (Booking) -> kotlin.Unit,
    private val onBookingClicked: (Booking) -> kotlin.Unit,
    private val onPayClicked:(Booking) -> kotlin.Unit,
    private val onDelBookingCLickListener: BookingAdapter.OnDelBookingCLickListener

) :
    RecyclerView.Adapter<BookingAdapter.BookingItemViewHolder>() {

    class BookingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingImage: ImageView = itemView.findViewById(R.id.unitImage)
        val payBooking: ImageView = itemView.findViewById(R.id.payBooking)
        val unitTitle: TextView = itemView.findViewById(R.id.unitTitle)
        val checkInDate: TextView = itemView.findViewById(R.id.checkInDate)
        val checkOutDate: TextView = itemView.findViewById(R.id.checkOutDate)
        val bookingId: TextView = itemView.findViewById(R.id.bookingId)
        val bookingStatus: TextView = itemView.findViewById(R.id.bookingStatus)
        val bookingAmount: TextView = itemView.findViewById(R.id.bookingAmount)
        val bookingDate: TextView = itemView.findViewById(R.id.bookingDate)
        val bookingAction: TextView = itemView.findViewById(R.id.bookingAction)
        val sendMsgBtn: TextView = itemView.findViewById(R.id.sendMsgBtn)
        val sendReviewBtn: TextView = itemView.findViewById(R.id.sendReviewBtn)
        val CancelBookingBtn: TextView = itemView.findViewById(R.id.canclebooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingItemViewHolder =
        BookingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.booking_item_layout,
                parent,
                false
            )
        )

    fun removebookedItem(bookingid: Int) {
        for (book in bookings) {
            if (book.id == bookingid) {
                bookings.remove(book)

                break
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bookings.size



    override fun onBindViewHolder(holder: BookingItemViewHolder, position: Int) {
        autoDispose.add(
            loadUnit(bookings[position].unitId).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { unit ->
                    activity?.let {
                        if (unit.attachments.isNotEmpty()) {
                            Glide.with(it).load(unit.attachments[0].image)
                                .into(holder.bookingImage)
                        }
                    }

                },
                {
                    Timber.e(it)
                })
        )
        if (isMyUnit) {
            // owner
            holder.sendMsgBtn.visibility = View.GONE
            if (bookings[position].status == 0) {
                holder.bookingAction.text = "Approve"
                holder.bookingAction.setOnClickListener {
                    bookings[position].status = 1
                    updateBookingState(bookings[position])
                    notifyDataSetChanged()
                }
            } else if (bookings[position].status == 1) {
                holder.bookingAction.text = "Key Delivered"
                holder.bookingAction.setOnClickListener {
                    bookings[position].status = 6
                    updateBookingState(bookings[position])
                    notifyDataSetChanged()
                }
            } else {
                holder.bookingAction.visibility = View.GONE
            }

        } else {
            // guest
            holder.sendMsgBtn.visibility = View.GONE
            if (bookings[position].status == 0 || bookings[position].status == 1 || bookings[position].status == 2) {
                holder.bookingAction.text = "Request Booking Cancel"
                holder.bookingAction.setOnClickListener {
                    bookings[position].status = -2
                    updateBookingState(bookings[position])
                    notifyDataSetChanged()
                }
            } else if (bookings[position].status == 6) {
                holder.bookingAction.text = "Confirm Key Delivered"
                holder.bookingAction.setOnClickListener {
                    bookings[position].status = 5
                    updateBookingState(bookings[position])
                    notifyDataSetChanged()
                }
            }else if (bookings[position].status == -5){
                holder.bookingAction.text = "Pay"
                holder.bookingAction.setOnClickListener {
                    onPayClicked(bookings[position])
                }
            } else {
                holder.bookingAction.visibility = View.GONE
            }

        }
        holder.payBooking.setOnClickListener {
           onPayClicked(bookings[position])
        }
        holder.unitTitle.text = bookings[position].unit.title
        holder.unitTitle.text = bookings[position].unit.title
        holder.checkInDate.text = bookings[position].dateStart
        holder.checkOutDate.text = bookings[position].dateEnd
        holder.bookingDate.text = bookings[position].createdAt.split(" ")[0]
        holder.sendMsgBtn.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.commit {
                replace(R.id.fragmentContainer, ChatMessagesFragment().apply {
                    arguments = bundleOf(
                        Pair(Constants.OWNER_ID_KEY, bookings[position].ownerId),
                        Pair(Constants.RECEIVER_ID_KEY, bookings[position].userId),
                        Pair(Constants.UNIT_ID_KEY, bookings[position].unit.id)
                    )
                    addToBackStack("")
                })
            }
        }

        holder.CancelBookingBtn.setOnClickListener {

            onDelBookingCLickListener.onDelClicked(bookings[position].id)

        }

        holder.bookingAmount.text = "${bookings[position].price} EGP"
        holder.bookingStatus.text = when (bookings[position].status) {
            -5 -> "UnPaid - Need Payment"
            -4 -> "Booking rejected"
            -3 -> "Cancel"
            -2 -> "Cancel Request"
            -1 -> "Closed - Expired"
            0 -> "Waiting  Approval"
            1 -> "Approved"
            2 -> "Paid"
            3 -> "Checkin"
            4 -> "Checkout"
            5 -> "Confirm Key Delivered"
            6 -> "Key Delivered"
            else -> "Accepted"
        }

//        holder.isAccepted.text = holder.bookingStatus.text
        holder.sendReviewBtn.setOnClickListener {
            onReviewClicked(bookings[position])
        }

        holder.itemView.setOnClickListener {
            onBookingClicked(bookings[position])
        }
        if (bookings[position].status == 4) {
            holder.sendReviewBtn.visibility = View.VISIBLE
        }else{
            holder.sendReviewBtn.visibility = View.GONE

        }

        holder.bookingId.text = "Ezuru#${bookings[position].id}"
    }

    public interface OnDelBookingCLickListener {
        fun onDelClicked(bookingId: Int)



    }
}