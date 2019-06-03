package com.markus.subscity.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markus.subscity.dagger.SubsCityDagger
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface

/**
 * @author Vitaliy Markus
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var slidrInterface: SlidrInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slidrInterface = Slidr.attach(this)
    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }
}