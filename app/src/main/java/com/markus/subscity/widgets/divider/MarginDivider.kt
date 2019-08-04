package com.markus.subscity.widgets.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * @author Vitaliy Markus
 */
class MarginDivider(private val context: Context) : RecyclerView.ItemDecoration() {

    private val ATTRS = intArrayOf(android.R.attr.listDivider)
    private var divider: Drawable
    private val bounds = Rect()

    var marginLeft = 0
    var marginRight = 0

    init {
        val typedArray = context.obtainStyledAttributes(ATTRS)
        divider = typedArray.getDrawable(0) ?: throw IllegalArgumentException("Drawable is null")
        typedArray.recycle()
    }

    fun setDrawable(drawable: Drawable) {
        divider = drawable
    }

    fun setDrawable(@DrawableRes drawableId: Int) {
        divider = ContextCompat.getDrawable(context, drawableId)
                ?: throw IllegalArgumentException("Drawable with ID = $drawableId must be not null")
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        drawVertical(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft + marginLeft
            right = parent.width - parent.paddingRight - marginRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + Math.round(child.translationY)
            val top = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter ?: return
        val position = parent.getChildAdapterPosition(view)
        if (position == adapter.itemCount - 1) {
            outRect.setEmpty()
        } else {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        }
    }
}