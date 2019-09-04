package com.markus.subscity.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.markus.subscity.R
import java.io.File

/**
 * @author Vitaliy Markus
 */
object FileUtils {

    private const val EXPORTED_PATH_NAME = "export"

    fun getFileExportPath(context: Context): File {
        return File(context.filesDir, EXPORTED_PATH_NAME)
    }

    fun getUriForShareFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, context.getString(R.string.file_export_provider), file)
    }
}
