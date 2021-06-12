package com.example.ezproject.ui.fragment.profile

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.ui.adapter.FavouriteAdapter
import com.example.ezproject.ui.adapter.MyUnitsAdapter
import com.example.ezproject.ui.adapter.UnitAdapter
import com.example.ezproject.ui.fragment.main.MainFragment
import com.example.ezproject.util.AutoDispose
import com.example.ezproject.util.Constants
import com.example.ezproject.util.MyApplication
import com.example.ezproject.util.extensions.handleApiError
import com.example.ezproject.util.extensions.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_user_unit.*
import kotlinx.android.synthetic.main.fragment_user_unit.backBtn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UserUnitsFragment : Fragment(),MyUnitsAdapter.OnDelCLickListener {

    @Inject
    @field:Named("profile")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_user_unit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          lateinit var viewAdapter: RecyclerView.Adapter<*>

        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragmentContainer, MainFragment())
                addToBackStack("")
            }
        }

        autoDispose.add(viewModel.userUnits().observeOn(AndroidSchedulers.mainThread()).subscribe({

            Log.d("uniiiiits",it.response.units.toString())
            unitsRecycler.adapter = MyUnitsAdapter(it.response.units, activity, viewModel.currency,this)
            unitsRecycler.layoutManager = LinearLayoutManager(context)
            val deleteIcon = resources.getDrawable(R.drawable.ic_option_on)
            val colorDrawableBackground = ColorDrawable(parseColor("#f7f7f7"))
             viewAdapter = unitsRecycler.adapter as MyUnitsAdapter

//48496
//            val itemTouchHelperCallback2 =
//                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//                    override fun onMove(
//                        recyclerView: RecyclerView,
//                        viewHolder: RecyclerView.ViewHolder,
//                        viewHolder2: RecyclerView.ViewHolder
//                    ): Boolean {
//                        return false
//                    }
//
//                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
//                        (viewAdapter as MyUnitsAdapter).removeItem(viewHolder.adapterPosition)
//                    }
//
//                    override fun onChildDraw(
//                        c: Canvas,
//                        recyclerView: RecyclerView,
//                        viewHolder: RecyclerView.ViewHolder,
//                        dX: Float,
//                        dY: Float,
//                        actionState: Int,
//                        isCurrentlyActive: Boolean
//                    ) {
//                        val itemView = viewHolder.itemView
//                        val iconMarginVertical = (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2
//
//                        if (dX > 0) {
//                            colorDrawableBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
//                            deleteIcon.setBounds(
//                                itemView.left + iconMarginVertical,
//                                itemView.top + iconMarginVertical,
//                                itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
//                                itemView.bottom - iconMarginVertical
//                            )
//                        } else {
//                            colorDrawableBackground.setBounds(
//                                itemView.right + dX.toInt(),
//                                itemView.top,
//                                itemView.right,
//                                itemView.bottom
//                            )
//                            deleteIcon.setBounds(
//                                itemView.right - iconMarginVertical - deleteIcon.intrinsicWidth,
//                                itemView.top + iconMarginVertical,
//                                itemView.right - iconMarginVertical,
//                                itemView.bottom - iconMarginVertical
//                            )
//                            deleteIcon.level = 0
//                        }
//
//                        colorDrawableBackground.draw(c)
//
//                        c.save()
//
//                        if (dX > 0)
//                            c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
//                        else
//                            c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
//
//                        deleteIcon.draw(c)
//
//                        c.restore()
//
//                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    }
//                }
//
//            val itemTouchHelper2 = ItemTouchHelper(itemTouchHelperCallback2)
//            itemTouchHelper2.attachToRecyclerView(unitsRecycler)
//


        }, {
            Timber.e(it)
            context?.handleApiError(it)
        }))
    }

    override fun onDelClicked(unitId: Int) {


                val options = arrayOf<CharSequence>("Yes", "Cancel")
                 val builder =
                     AlertDialog.Builder(activity!!)
                 builder.setTitle("Delete this unit!");
                 builder.setItems(options) { dialog, item ->
                     if (options[item].equals("Yes")) {

                         autoDispose.add(
                             viewModel.deleteundraft(unitId).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                 {
                                     (unitsRecycler.adapter as MyUnitsAdapter).removeItem(unitId)

                                     if (it.status == Constants.STATUS_OK) {
                                         context?.makeToast(it.message!!)
                                     }
                                 },
                                 {
                                     Timber.e(it)
                                     context?.handleApiError(it)
                                  })
                         )

                     }  else if (options[item].equals("Cancel")) {
                         dialog.dismiss()
                     }
                 }
                 builder.show()

    }
}