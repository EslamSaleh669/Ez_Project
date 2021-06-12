package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Unit
import com.example.ezproject.data.network.response.UnitsResponse
import com.example.ezproject.data.network.response.WishListResponse
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.roundPrice
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class FavouriteAdapter(
    private val activity: Activity?,
    private var units: ArrayList<WishListResponse.Response.Data>,
    private val disposableObj: AutoDispose,
    private val currency: String,
    private val onFavCLickListener: OnFavCLickListener,
    private val loadingFun: (unitId: Int) -> Observable<Unit>
) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder =
        FavouriteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.favourite_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = units.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        holder.favBtn.setOnClickListener {
            onFavCLickListener.onFavClicked(units[position].unitId)
        }
        holder.itemView.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.commit {
                replace(
                    R.id.fragmentContainer,
                    UnitDetailsFragment.instance(units[position].unitId)
                )
                addToBackStack("")
            }
        }
        disposableObj.add(
            loadingFun(units[position].unitId).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { unit ->
                    activity?.let {
                        if (unit.attachments.isNotEmpty()) {
                            Glide.with(it).load(unit.attachments[0].image).into(holder.unitImage)
                        }
                    }
                    holder.unitTitle.text = unit.title
                    holder.unitPrice.text = "${unit.price.roundPrice()} $currency"
                    holder.unitAddress.text = unit.address


                },
                {
                    Timber.e(it)
                    activity?.handleApiError(it)
                })

        )

    }


    fun removeItem(unitId: Int) {
        for (unit in units) {
            if (unit.unitId == unitId) {
                units.remove(unit)

                break
            }
        }
        notifyDataSetChanged()
    }

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.unitImage)
        val unitTitle: TextView = itemView.findViewById(R.id.unitTitle)
        val favBtn: ImageView = itemView.findViewById(R.id.favBtn)
        val unitPrice: TextView = itemView.findViewById(R.id.unitPrice)
        val unitAddress: TextView = itemView.findViewById(R.id.address)
    }

    public interface OnFavCLickListener {
        fun onFavClicked(unitId: Int)
    }
}