package com.source.subscity.ui.cinema

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.source.subscity.ui.movie.MovieActivity

/**
 * @author Vitaliy Markus
 */
class CinemaActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_CINEMA_ID = "cinema_id"

        fun start(context: Context, cinemaId: Long) {
            val intent = Intent(context, CinemaActivity::class.java)
                    .putExtra(EXTRA_CINEMA_ID, cinemaId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, CinemaFragment.newInstance(intent.getLongExtra(EXTRA_CINEMA_ID, 0)))
                    .commit()
        }
    }
}