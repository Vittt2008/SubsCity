package com.source.subscity.widgets.transformations

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.PAINT_FLAGS
import com.bumptech.glide.util.Synthetic
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author Vitaliy Markus
 */
class Crop : BitmapTransformation() {

    companion object {
        private const val ID = "com.source.subscity.widgets.transformations.Crop"
        private val ID_BYTES = ID.toByteArray(CHARSET)
        private val DEFAULT_PAINT = Paint(PAINT_FLAGS)
        private val MODELS_REQUIRING_BITMAP_LOCK = HashSet(
                Arrays.asList(
                        // Moto X gen 2
                        "XT1085",
                        "XT1092",
                        "XT1093",
                        "XT1094",
                        "XT1095",
                        "XT1096",
                        "XT1097",
                        "XT1098",
                        // Moto G gen 1
                        "XT1031",
                        "XT1028",
                        "XT937C",
                        "XT1032",
                        "XT1008",
                        "XT1033",
                        "XT1035",
                        "XT1034",
                        "XT939G",
                        "XT1039",
                        "XT1040",
                        "XT1042",
                        "XT1045",
                        // Moto G gen 2
                        "XT1063",
                        "XT1064",
                        "XT1068",
                        "XT1069",
                        "XT1072",
                        "XT1077",
                        "XT1078",
                        "XT1079"
                )
        )

        private val BITMAP_DRAWABLE_LOCK = if (MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL))
            ReentrantLock()
        else
            NoLock()
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return centerCrop(pool, toTransform, outWidth, outHeight)
    }

    override fun equals(other: Any?): Boolean {
        return other is Crop
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    private fun centerCrop(pool: BitmapPool, inBitmap: Bitmap, width: Int,
                           height: Int): Bitmap {
        if (inBitmap.width == width && inBitmap.height == height) {
            return inBitmap
        }
        // From ImageView/Bitmap.createScaledBitmap.
        val scale: Float
        val dx: Float
        val dy: Float
        val m = Matrix()
        if (inBitmap.width * height > width * inBitmap.height) {
            scale = height.toFloat() / inBitmap.height.toFloat()
            dx = (width - inBitmap.width * scale) * 0.5f
            dy = 0f
        } else {
            scale = width.toFloat() / inBitmap.width.toFloat()
            dx = 0f
            dy = (height - inBitmap.height * scale) * 0.5f
        }

        m.setScale(scale, scale)
        m.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())

        val result = pool.get(width, height, getNonNullConfig(inBitmap))
        // We don't add or remove alpha, so keep the alpha setting of the Bitmap we were given.
        TransformationUtils.setAlpha(inBitmap, result)

        applyMatrix(inBitmap, result, m)
        return result
    }

    private fun getNonNullConfig(bitmap: Bitmap): Bitmap.Config {
        return if (bitmap.config != null) bitmap.config else Bitmap.Config.ARGB_8888
    }

    private fun applyMatrix(inBitmap: Bitmap, targetBitmap: Bitmap,
                            matrix: Matrix) {
        BITMAP_DRAWABLE_LOCK.lock()
        try {
            val canvas = Canvas(targetBitmap)
            canvas.drawBitmap(inBitmap, matrix, DEFAULT_PAINT)
            clear(canvas)
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock()
        }
    }

    private fun clear(canvas: Canvas) {
        canvas.setBitmap(null)
    }

    private class NoLock @Synthetic constructor() : Lock {

        override fun lock() {
            // do nothing
        }

        @Throws(InterruptedException::class)
        override fun lockInterruptibly() {
            // do nothing
        }

        override fun tryLock(): Boolean {
            return true
        }

        @Throws(InterruptedException::class)
        override fun tryLock(time: Long, unit: TimeUnit): Boolean {
            return true
        }

        override fun unlock() {
            // do nothing
        }

        override fun newCondition(): Condition {
            throw UnsupportedOperationException("Should not be called")
        }
    }
}