package com.source.subscity.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.source.subscity.R

/**
 * @author Vitaliy Markus
 */
object IntentUtils {

    private val PHONE_RES_SCHEME = "tel:"

    fun createShareTextIntent(context: Context, subject: String, shareBody: String): Intent {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"

        if (!TextUtils.isEmpty(subject)) {
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        return Intent.createChooser(sharingIntent, context.getString(R.string.send))
    }

    fun createShareImageIntent(context: Context, imageUri: Uri): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"

        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        return Intent.createChooser(shareIntent, context.getString(R.string.send))
    }

    fun createShareTextWithImageIntent(context: Context, subject: String?, shareBody: String, imageUri: Uri): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"

        if (!TextUtils.isEmpty(subject)) {
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return Intent.createChooser(shareIntent, context.getString(R.string.send))
    }

    fun createOpenDialerIntent(phoneNumber: String): Intent {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("$PHONE_RES_SCHEME:$phoneNumber")
        return intent
    }
}

