package com.markus.subscity.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.markus.subscity.R
import com.markus.subscity.utils.BaseActivity
import java.io.InputStreamReader

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
        setContentView(R.layout.activity_polocy)
        val policyView = findViewById<TextView>(R.id.tv_policy)
        policyView.text = fromHtml(getPolicy())
    }

    private fun fromHtml(text: String): Spanned {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Html.fromHtml(text)
        } else {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    }

    private fun getPolicy(): String {
        val inputStream = resources.openRawResource(R.raw.privacy_policy)
        val text = InputStreamReader(inputStream).readText()
        return text
    }
}