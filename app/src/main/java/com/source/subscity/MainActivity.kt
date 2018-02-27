package com.source.subscity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.source.subscity.ui.cinemas.CinemasFragment
import com.source.subscity.ui.movies.MoviesFragment
import com.source.subscity.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, SettingsFragment())
                    .commit()
        }
    }


}
