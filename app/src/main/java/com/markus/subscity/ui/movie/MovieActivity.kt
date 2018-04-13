package com.markus.subscity.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.markus.subscity.utils.BaseActivity

/**
 * @author Vitaliy Markus
 */
class MovieActivity : BaseActivity() {

    companion object {

        const val EXTRA_MOVIE_ID = "movie_id"

        fun start(context: Context, movieId: Long) {
            val intent = Intent(context, MovieActivity::class.java)
                    .putExtra(EXTRA_MOVIE_ID, movieId)
            context.startActivity(intent)
        }

        fun createIntent(context: Context, movieId: Long): Intent {
            return Intent(context, MovieActivity::class.java)
                    .putExtra(EXTRA_MOVIE_ID, movieId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, MovieFragment.newInstance(intent.getLongExtra(EXTRA_MOVIE_ID, 0)))
                    .commit()
        }
    }
}