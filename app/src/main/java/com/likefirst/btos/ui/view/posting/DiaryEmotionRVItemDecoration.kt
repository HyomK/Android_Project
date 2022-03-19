package com.likefirst.btos.ui.view.posting

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DiaryEmotionRVItemDecoration : RecyclerView.ItemDecoration(){

    private var size5 = 0
    private var size30 = 0

    fun setSize(context: Context) {
        size5 = dpToPx(context, 5)
        size30 = dpToPx(context, 30)
    }

    // dp -> pixel 단위로 변경
    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == 0){
            outRect.left = size30
            outRect.right = size5
        } else if (position == itemCount-1){
            outRect.right = size30
            outRect.left = size5
        } else {
            outRect.left = size5
            outRect.right = size5
        }
    }
}