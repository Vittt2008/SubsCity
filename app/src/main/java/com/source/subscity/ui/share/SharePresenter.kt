package com.source.subscity.ui.share

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.utils.FileUtils
import com.source.subscity.utils.ImageUtils
import com.source.subscity.utils.IntentUtils
import java.io.File

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SharePresenter : MvpPresenter<ShareView>() {

    private val POSTER_NAME = "temp_poster.jpg"

    fun createShareIntent(context: Context, drawable: Drawable?, title: String, content: String): Intent {
        val file = if (drawable != null) exportDrawable(context, drawable) else null
        val intent = createIntent(context, file, title, content)
        return intent
    }

    fun exportDrawable(context: Context, drawable: Drawable): File? {
        try {
            val bitmap = ImageUtils.drawableToBitmap(drawable)
            val file = ImageUtils.saveBitmapForExport(context, bitmap, POSTER_NAME)
            return file
        } catch (e: Exception) {
            return null
        }

    }

    fun createIntent(context: Context, file: File?, title: String, content: String): Intent {
        if (file != null && file.exists()) {
            val image = FileUtils.getUriForShareFile(context, file)
            return IntentUtils.createShareTextWithImageIntent(context, title, content, image)
        } else {
            return IntentUtils.createShareTextIntent(context, title, content)
        }
    }
}
