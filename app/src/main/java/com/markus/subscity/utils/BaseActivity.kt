package com.markus.subscity.utils

import android.support.v7.app.AppCompatActivity
import com.markus.subscity.dagger.SubsCityDagger

/**
 * @author Vitaliy Markus
 */
open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }
}