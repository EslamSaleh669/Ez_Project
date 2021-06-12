package com.example.ezproject.util
//
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.RectF
//import android.view.MotionEvent
//import android.view.View
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.ItemTouchHelper.*
//import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags
//import androidx.recyclerview.widget.RecyclerView
//
//
//internal enum class ButtonsState {
//    GONE, LEFT_VISIBLE, RIGHT_VISIBLE
//}
//
//class SwipeController(buttonsActions: SwipeControllerActions?) :
//    ItemTouchHelper.Callback() {
//    private var swipeBack = false
//    private var buttonShowedState = ButtonsState.GONE
//    private var buttonInstance: RectF? = null
//    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
//    private val buttonsActions: SwipeControllerActions? = null
//
//    override fun  getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//        return makeMovementFlags(
//            0,
//            LEFT or RIGHT
//        )
//    }
//
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean {
//        return false
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
//    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
//        if (swipeBack) {
//            swipeBack = buttonShowedState != ButtonsState.GONE
//            return 0
//        }
//        return super.convertToAbsoluteDirection(flags, layoutDirection)
//    }
//
//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        var dX = dX
//        if (actionState == ACTION_STATE_SWIPE) {
//            if (buttonShowedState != ButtonsState.GONE) {
//                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX =
//                    Math.max(dX, buttonWidth)
//                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX =
//                    Math.min(dX, -buttonWidth)
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            } else {
//                setTouchListener(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            }
//        }
//        if (buttonShowedState == ButtonsState.GONE) {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        }
//        currentItemViewHolder = viewHolder
//    }
//
//    private fun setTouchListener(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        recyclerView.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent): Boolean {
//                swipeBack =
//                    event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
//                if (swipeBack) {
//                    if (dX < -buttonWidth) buttonShowedState =
//                        ButtonsState.RIGHT_VISIBLE else if (dX > buttonWidth) buttonShowedState =
//                        ButtonsState.LEFT_VISIBLE
//                    if (buttonShowedState != ButtonsState.GONE) {
//                        setTouchDownListener(
//                            c,
//                            recyclerView,
//                            viewHolder,
//                            dX,
//                            dY,
//                            actionState,
//                            isCurrentlyActive
//                        )
//                        setItemsClickable(recyclerView, false)
//                    }
//                }
//                return false
//            }
//        })
//    }
//
//    private fun setTouchDownListener(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        recyclerView.setOnTouchListener(object : View.OnTouchListener() {
//            override fun onTouch(v: View?, event: MotionEvent): Boolean {
//                if (event.action == MotionEvent.ACTION_DOWN) {
//                    setTouchUpListener(
//                        c,
//                        recyclerView,
//                        viewHolder,
//                        dX,
//                        dY,
//                        actionState,
//                        isCurrentlyActive
//                    )
//                }
//                return false
//            }
//        })
//    }
//
//    private fun setTouchUpListener(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        recyclerView.setOnTouchListener(object : View.OnTouchListener {
//           override fun onTouch(v: View?, event: MotionEvent): Boolean {
//                if (event.action == MotionEvent.ACTION_UP) {
//                    super@SwipeController.onChildDraw(
//                        c,
//                        recyclerView,
//                        viewHolder,
//                        0f,
//                        dY,
//                        actionState,
//                        isCurrentlyActive
//                    )
//                    recyclerView.setOnTouchListener(object : View.OnTouchListener {
//                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                            return false
//                        }
//                    })
//                    setItemsClickable(recyclerView, true)
//                    swipeBack = false
//                    if (buttonsActions != null && buttonInstance != null && buttonInstance!!.contains(
//                            event.x,
//                            event.y
//                        )
//                    ) {
//                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//                            buttonsActions.onLeftClicked(viewHolder.adapterPosition)
//                        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//                            buttonsActions.onRightClicked(viewHolder.adapterPosition)
//                        }
//                    }
//                    buttonShowedState = ButtonsState.GONE
//                    currentItemViewHolder = null
//                }
//                return false
//            }
//        })
//    }
//
//    private fun setItemsClickable(
//        recyclerView: RecyclerView,
//        isClickable: Boolean
//    ) {
//        for (i in 0 until recyclerView.childCount) {
//            recyclerView.getChildAt(i).isClickable = isClickable
//        }
//    }
//
//    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
//        val buttonWidthWithoutPadding = buttonWidth - 20
//        val corners = 16f
//        val itemView: View = viewHolder.itemView
//        val p = Paint()
//        val leftButton = RectF(
//            itemView.getLeft(),
//            itemView.getTop(),
//            itemView.getLeft() + buttonWidthWithoutPadding,
//            itemView.getBottom()
//        )
//        p.setColor(Color.BLUE)
//        c.drawRoundRect(leftButton, corners, corners, p)
//        drawText("EDIT", c, leftButton, p)
//        val rightButton = RectF(
//            itemView.getRight() - buttonWidthWithoutPadding,
//            itemView.getTop(),
//            itemView.getRight(),
//            itemView.getBottom()
//        )
//        p.setColor(Color.RED)
//        c.drawRoundRect(rightButton, corners, corners, p)
//        drawText("DELETE", c, rightButton, p)
//        buttonInstance = null
//        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//            buttonInstance = leftButton
//        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//            buttonInstance = rightButton
//        }
//    }
//
//    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint) {
//        val textSize = 60f
//        p.setColor(Color.WHITE)
//        p.setAntiAlias(true)
//        p.setTextSize(textSize)
//        val textWidth: Float = p.measureText(text)
//        c.drawText(text, button.centerX() - textWidth / 2, button.centerY() + textSize / 2, p)
//    }
//
//    fun onDraw(c: Canvas) {
//        if (currentItemViewHolder != null) {
//            drawButtons(c, currentItemViewHolder!!)
//        }
//    }
//
//    companion object {
//        private const val buttonWidth = 300f
//    }
//
//}