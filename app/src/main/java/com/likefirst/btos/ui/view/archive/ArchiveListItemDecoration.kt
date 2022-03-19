package com.likefirst.btos.ui.view.archive

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ArchiveListItemDecoration: RecyclerView.ItemDecoration() {
    private var size20 = 0

    fun setSize(context: Context) {
        size20 = dpToPx(context, 20)
    }

    // dp -> pixel 단위로 변경
    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val viewHolder = parent.getChildViewHolder(view)

        if (viewHolder is ArchiveListRVAdapter.DiaryViewHolder){
            outRect.bottom = size20
        }
    }
}