package com.markus.subscity.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markus.subscity.dagger.SubsCityDagger
import com.r0adkll.slidr.Slidr

/**
 * @author Vitaliy Markus
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }
}