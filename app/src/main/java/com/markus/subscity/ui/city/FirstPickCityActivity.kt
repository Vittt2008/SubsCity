package com.markus.subscity.ui.city

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markus.subscity.dagger.SubsCityDagger

/**
 * @author Vitaliy Markus
 */
class FirstPickCityActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, FirstPickCityActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, CityFragment.newInstance(true))
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }
}