package com.markus.subscity.ui.city

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class CityActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CityActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, CityFragment.newInstance())
                    .commit()
        }
    }
}