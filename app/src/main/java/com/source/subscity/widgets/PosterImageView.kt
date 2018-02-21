package com.source.subscity.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView

/**
 * @author Vitaliy Markus
 */
class PosterImageView : ImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setImageBitmap(bm: Bitmap?) {
//        if (bm != null) {
//            updateMatrix(bm)
//        }
        super.setImageBitmap(bm)
    }

    override fun setImageDrawable(drawable: Drawable?) {
//        if (drawable != null && drawable is BitmapDrawable && drawable.bitmap != null) {
//            updateMatrix(drawable.bitmap)
//        }
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
    }

    private fun updateMatrix(inBitmap: Bitmap) {
        val scale: Float
        val dx: Float
        val dy: Float
        val m = Matrix()
        if (inBitmap.width * height > width * inBitmap.height) {
            scale = height.toFloat() / inBitmap.height.toFloat()
            dx = (width - inBitmap.width * scale) * 0.2f
            dy = 0f
        } else {
            scale = width.toFloat() / inBitmap.width.toFloat()
            dx = 0f
            dy = (height - inBitmap.height * scale) * 0.2f
        }

        m.setScale(scale, scale)
        m.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())
        imageMatrix = m
    }
}