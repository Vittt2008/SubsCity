package com.markus.subscity.ui.donate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.markus.subscity.R
import com.markus.subscity.controllers.ContentLoadingController
import com.markus.subscity.extensions.toast
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class DonateActivity : BaseActivity(), BillingProcessor.IBillingHandler {

    private lateinit var billingProcessor: BillingProcessor
    private lateinit var loadingController: ContentLoadingController

    private lateinit var donatesList: RecyclerView

    companion object {
        const val LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi5lzOD4D0Lg4vPUhX+qb5HWhDb66fa0+i6ginNFkWFRH1dzx9Nv6++LwfzpF1UiAJIDiS7jAllDfssgqF8a67XXgAUJ/c62sLLvhD/XO9ZhMxNQA+FMR3SZW/yj4AXi7NNz1dT/mU9wmOeJXbetWTsqG+GI3Ww25+E4WH8AqCQ9WpXH5oIkOFJpN6PHob80g5rMIXHMxdUbfHCfXwz3f9XTGtzkMFz81hyT45pgfa4iggsjkuEVHzMX9W42TQmhb/SEgaPBw8vUa905OVDvr40WRw6oCwa/u4XA6WTViDBP0EiZ3KvV6Z267Gd7zlwJJbYfXv070mgoEKDfH3qUxoQIDAQAB"
        const val PRODUCT_TEA = "donate_tea"
        const val PRODUCT_COFFEE = "donate_coffee"
        const val PRODUCT_MOVIE_TICKET = "donate_movie_ticket"


        fun start(context: Context) {
            val intent = Intent(context, DonateActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_donate)
        initToolbar()
        donatesList = findViewById(R.id.rv_list)
        loadingController = ContentLoadingController(this, R.id.rv_list, R.id.pb_progress)
        billingProcessor = BillingProcessor(this, LICENSE_KEY, this)
        loadingController.setContentState(ContentLoadingController.State.PROGRESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBillingInitialized() {
        val details = billingProcessor.getPurchaseListingDetails(ArrayList(listOf(PRODUCT_TEA, PRODUCT_COFFEE, PRODUCT_MOVIE_TICKET)))
        donatesList.layoutManager = LinearLayoutManager(this)
        donatesList.adapter = DonateAdapter(details.sortedBy { it.priceLong }) { billingProcessor.purchase(this, it.productId) }
        loadingController.switchState(ContentLoadingController.State.CONTENT)
    }

    override fun onPurchaseHistoryRestored() {
        toast("RESTORED")
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        val string = "ProductId = $productId; Details = ${details?.toString()}"
        Log.e("PURCHASED", string)
        toast("PURCHASED __ $string")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        val string = "ErrorCode = $errorCode; Error = ${error?.toString()}"
        Log.e("ERROR", string)
        toast("ERROR __ $string")
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}