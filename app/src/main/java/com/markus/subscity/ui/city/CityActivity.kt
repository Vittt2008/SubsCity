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
        const val EXTRA_FIRST_PICK = "first_pick"

        fun start(context: Context, firstPick: Boolean) {
            val intent = Intent(context, CityActivity::class.java)
                    .putExtra(EXTRA_FIRST_PICK, firstPick)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, CityFragment.newInstance(intent.getBooleanExtra(EXTRA_FIRST_PICK, false)))
                    .commit()
        }
    }
}