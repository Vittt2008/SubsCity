package com.markus.subscity.ui.donate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.markus.subscity.extensions.toast
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class DonateActivity : BaseActivity(), BillingProcessor.IBillingHandler {

    private lateinit var billingProcessor: BillingProcessor

    companion object {
        const val LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi5lzOD4D0Lg4vPUhX+qb5HWhDb66fa0+i6ginNFkWFRH1dzx9Nv6++LwfzpF1UiAJIDiS7jAllDfssgqF8a67XXgAUJ/c62sLLvhD/XO9ZhMxNQA+FMR3SZW/yj4AXi7NNz1dT/mU9wmOeJXbetWTsqG+GI3Ww25+E4WH8AqCQ9WpXH5oIkOFJpN6PHob80g5rMIXHMxdUbfHCfXwz3f9XTGtzkMFz81hyT45pgfa4iggsjkuEVHzMX9W42TQmhb/SEgaPBw8vUa905OVDvr40WRw6oCwa/u4XA6WTViDBP0EiZ3KvV6Z267Gd7zlwJJbYfXv070mgoEKDfH3qUxoQIDAQAB"

        fun start(context: Context) {
            val intent = Intent(context, DonateActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .add(android.R.id.content, DonateFragment.newInstance())
//                    .commit()
//        }

        billingProcessor = BillingProcessor(this, DonateFragment.LICENSE_KEY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBillingInitialized() {
        toast("SUCCESS")
        val listOwnedProducts = billingProcessor.getPurchaseListingDetails("donate_tea")
    }

    override fun onPurchaseHistoryRestored() {
        toast("RESTORED")
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        toast("PURCHASED")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        toast("ERROR")
    }
}