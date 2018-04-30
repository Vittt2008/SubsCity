package com.markus.subscity.ui.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.setSupportActionBar
import com.markus.subscity.extensions.toast

/**
 * @author Vitaliy Markus
 */
class DonateFragment : MvpAppCompatFragment(), DonateView, BillingProcessor.IBillingHandler {

    @InjectPresenter
    lateinit var donatePresenter: DonatePresenter
    private lateinit var billingProcessor: BillingProcessor

    companion object {
        const val LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi5lzOD4D0Lg4vPUhX+qb5HWhDb66fa0+i6ginNFkWFRH1dzx9Nv6++LwfzpF1UiAJIDiS7jAllDfssgqF8a67XXgAUJ/c62sLLvhD/XO9ZhMxNQA+FMR3SZW/yj4AXi7NNz1dT/mU9wmOeJXbetWTsqG+GI3Ww25+E4WH8AqCQ9WpXH5oIkOFJpN6PHob80g5rMIXHMxdUbfHCfXwz3f9XTGtzkMFz81hyT45pgfa4iggsjkuEVHzMX9W42TQmhb/SEgaPBw8vUa905OVDvr40WRw6oCwa/u4XA6WTViDBP0EiZ3KvV6Z267Gd7zlwJJbYfXv070mgoEKDfH3qUxoQIDAQAB"
        fun newInstance(): DonateFragment {
            return DonateFragment()
        }
    }

    @ProvidePresenter
    fun donateFragment(): DonatePresenter {
        return SubsCityDagger.component.createDonatePresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        billingProcessor = BillingProcessor(activity, LICENSE_KEY, this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_donate, container, false)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun onBillingInitialized() {
        toast("SUCCESS")
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