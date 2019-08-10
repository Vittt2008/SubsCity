package com.markus.subscity.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.markus.subscity.R
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class ThemeActivity : BaseActivity() {

    companion object {
        private const val EXTRA_CHANGING_THEME = "changing_theme"

        fun start(context: Context) {
            context.startActivity(createIntent(context, changingTheme = false))
        }

        fun createIntent(context: Context, changingTheme: Boolean): Intent {
            return Intent(context, ThemeActivity::class.java).apply {
                putExtra(EXTRA_CHANGING_THEME, changingTheme)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, ThemeFragment.newInstance())
                    .commit()
        }
    }
}