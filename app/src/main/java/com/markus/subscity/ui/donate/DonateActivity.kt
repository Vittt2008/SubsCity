package com.markus.subscity.ui.donate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class DonateActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DonateActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, DonateFragment.newInstance())
                    .commit()
        }
    }
}