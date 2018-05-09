package com.markus.subscity.providers

import android.content.Context
import com.anjlab.android.iab.v3.BillingProcessor
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class BillingProvider @Inject constructor(private val context: Context) {

    val isBillingAvailable: Boolean
        get() = BillingProcessor.isIabServiceAvailable(context)
}