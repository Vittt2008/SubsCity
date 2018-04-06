package com.source.subscity.utils

import android.content.Context
import android.net.Uri
import android.support.v4.content.FileProvider
import com.source.subscity.R
import java.io.File

/**
 * @author Vitaliy Markus
 */
object FileUtils {

    private val EXPORTED_PATH_NAME = "export"

    fun getFileExportPath(context: Context): File {
        return File(context.filesDir, EXPORTED_PATH_NAME)
    }

    fun getUriForShareFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, context.getString(R.string.file_export_provider), file)
    }
}
