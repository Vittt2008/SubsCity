package com.source.subscity.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class DrawableExporter @Inject constructor(private val context: Context) {

    fun export(drawable: Drawable, fileName: String): File? {
        return try {
            saveBitmapForExport(toBitmap(drawable), fileName)
        } catch (e: Exception) {
            null
        }
    }

    private fun toBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun saveBitmapForExport(bitmap: Bitmap, fileName: String): File {
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