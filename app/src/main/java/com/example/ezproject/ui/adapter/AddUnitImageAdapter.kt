package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.network.response.ImageUploadResponse
import com.example.ezproject.ui.activity.MainActivity

class AddUnitImageAdapter(
    val images: ArrayList<ImageUploadResponse>,
    val activity: Activity?,
    val onAddClickListener: OnAddClickListener,
    val onDelete: (position: Int) -> Unit,
    val onupdate: (position: Int) -> Unit
) :
    RecyclerView.Adapter<AddUnitImageAdapter.ImageItemViewHolder>() {

    class ImageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageItem: ImageView? = itemView.findViewById(R.id.imageItem)
        val addImages: FrameLayout? = itemView.findViewById(R.id.addImages)
        val deleteBtn: ImageView? = itemView.findViewById(R.id.deleteBtn)
    }

    override fun getItemViewType(position: Int): Int {
        return if (images[position].url == "") -1 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {

        return ImageItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (viewType == 1) R.layout.unit_image_item_layout else R.layout.add_image_item_layout,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        if (holder.itemViewType == 1) {
            activity?.let {
                Glide.with(it).load(images[position].url).into(holder.imageItem!!)
            }

            holder.itemView.setOnClickListener {
                onupdate(position)
            }
            holder.deleteBtn?.setOnClickListener {
                onDelete(position)
                images.removeAt(position)
                notifyDataSetChanged()
            }
        } else {
            holder.addImages?.setOnClickListener {
                onAddClickListener.onAddClick()
            }
        }
    }

    fun updateImage(position: Int, image: ImageUploadResponse) {
        images[position] = image
        notifyDataSetChanged()
    }

    fun addImage(image: ImageUploadResponse) {
        if (images.isNotEmpty()) {
            images.remove(images.last())
            images.add(image)
            images.add(ImageUploadResponse("", true, ""))
        } else {
            images.add(image)
            images.add(ImageUploadResponse("", true, ""))
        }
        notifyDataSetChanged()
    }


    interface OnAddClickListener {
        fun onAddClick()
    }
}