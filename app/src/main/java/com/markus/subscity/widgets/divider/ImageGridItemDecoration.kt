package com.markus.subscity.widgets.divider

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
 *
 * @author Vitaliy Markus
 */
class ImageGridItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount
            if (position + 1 == parent.adapter.itemCount) {
                outRect.right = 0
            } else {
                outRect.right = spacing - (column + 1) * spacing / spanCount
            }
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}
