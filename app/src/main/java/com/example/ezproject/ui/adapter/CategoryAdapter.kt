package com.example.ezproject.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ezproject.R
import com.example.ezproject.data.models.Category
import com.example.ezproject.data.repo.UserRepo
import com.example.ezproject.ui.activity.auth.splash.SplashViewModel
import com.example.ezproject.ui.fragment.AllCorporatesFragment
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.util.Constants
import javax.inject.Inject
import javax.inject.Named

class CategoryAdapter(private val categories: List<Category>, val activity: Activity? , val type : Int) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_item_layout,
                parent,
                false
            )
        )


    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
       val lan = activity?.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)?.getString(Constants.LANG_KEY,"en")

        if (lan.equals("ar")){
            holder.categoryName.text = categories[position].name
        }else{
            holder.categoryName.text = categories[position].nameEn

        }

        activity?.let {
            Glide.with(it).load(categories[position].photo).into(holder.categoryImage)
        }

        holder.itemView.setOnClickListener {




            (activity as AppCompatActivity).supportFragmentManager.commit {
                if (type == 1){
                    replace(R.id.fragmentContainer,
                        AllUnitsFragment().apply {
                            arguments = bundleOf(Pair(Constants.CATE_ID_KEY, categories[position].id))
                        }
                    )
                    addToBackStack("")
                }else{
                    val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
                        Constants.Tosaveit,
                        0
                    )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putInt("categoryid",categories[position].id)
                    editor.putInt("myplace",1)
                    editor.apply()
                    replace(R.id.fragmentContainer,
                        AllCorporatesFragment().apply {
                            arguments = bundleOf(
                                Pair(Constants.CATE_ID_KEY, categories[position].id),
                                Pair(Constants.FROMWHERE, 1)

                                )
                        }
                    )
                    addToBackStack("")
                }

            }
        }
    }
}