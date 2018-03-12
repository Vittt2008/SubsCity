package com.source.subscity.ui.deeplink

import android.app.Activity

/**
 * @author Vitaliy Markus
 */
val Activity.isFromDeepLink: Boolean
    get() {
        val data = this.intent.data
        return data != null
    }