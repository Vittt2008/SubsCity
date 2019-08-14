package com.markus.subscity.widgets.divider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-horizontalSpacing
 *
 * @author Vitaliy Markus
 */
class ImageGridItemDecoration(private val spanCount: Int,
                              private val horizontalSpacing: Int,
                              private val verticalSpacing: Int,
                              private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount
            outRect.right = (column + 1) * horizontalSpacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = verticalSpacing
            }
            outRect.bottom = verticalSpacing // item bottom
        } else {
            outRect.left = column * horizontalSpacing / spanCount
            if (position + 1 == parent.adapter?.itemCount) {
                outRect.right = 0
            } else {
                outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / spanCount
            }
            if (position >= spanCount) {
                outRect.top = verticalSpacing // item top
            }
        }
    }
}
