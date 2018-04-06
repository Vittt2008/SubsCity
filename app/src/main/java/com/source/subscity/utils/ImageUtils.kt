package com.source.subscity.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author Vitaliy Markus
 */
object ImageUtils {

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    @Throws(IOException::class)
    fun saveBitmapForExport(context: Context, bitmap: Bitmap, fileName: String): File {
        val path = FileUtils.getFileExportPath(context)
        if (!path.exists()) {
            path.mkdirs()
        }

        val file = File(path, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        outputStream.close()

        return file
    }

}

