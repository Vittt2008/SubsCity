package com.markus.subscity.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import com.markus.subscity.R
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class PolicyActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PolicyActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_policy)
        val policyView = findViewById<WebView>(R.id.wv_policy)
        policyView.loadUrl("file:///android_asset/privacy_policy.html");
    }
}