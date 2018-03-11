package com.source.subscity.ui.youtube

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.source.subscity.R
import com.source.subscity.extensions.toast
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE


/**
 * @author Vitaliy Markus
 */
class YouTubeActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var youTubeFragment: YouTubePlayerSupportFragment

    companion object {

        private const val EXTRA_TRAILER_ID = "trailer_id"
        private const val RECOVERY_DIALOG_REQUEST_CODE = 1
        private const val YOU_TUBE_FRAGMENT_TAG = "you_tube_fragment_tag"
        private const val YOUTUBE_API_KEY = "AIzaSyCqCqzIErRcvk6-m6uGsuwcDEzN-jULAmQ" //TODO Replace Key

        fun start(context: Context, trailerId: String) {
            val intent = Intent(context, YouTubeActivity::class.java)
                    .putExtra(EXTRA_TRAILER_ID, trailerId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        if (savedInstanceState == null) {
            youTubeFragment = YouTubePlayerSupportFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, youTubeFragment, YOU_TUBE_FRAGMENT_TAG)
                    .commit()
        } else {
            youTubeFragment = supportFragmentManager.findFragmentByTag(YOU_TUBE_FRAGMENT_TAG) as YouTubePlayerSupportFragment
        }

        youTubeFragment.initialize(YOUTUBE_API_KEY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST_CODE) {
            // Retry initialization if user performed a recovery action
            youTubeFragment.initialize(YOUTUBE_API_KEY, this)
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            player.setFullscreen(true)
            player.cueVideo(intent.getStringExtra(EXTRA_TRAILER_ID))
            player.setShowFullscreenButton(false)
            player.play()
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST_CODE).show()
        } else {
            toast(getString(R.string.youtube_error_player, errorReason.toString()))
        }
    }

}