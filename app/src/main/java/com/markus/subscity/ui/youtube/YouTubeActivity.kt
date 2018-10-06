package com.markus.subscity.ui.youtube

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import com.google.android.youtube.player.*
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.toast


/**
 * @author Vitaliy Markus
 */
class YouTubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var playerView: YouTubePlayerView

    companion object {
        private const val EXTRA_TRAILER_ID = "trailer_id"
        private const val RECOVERY_DIALOG_REQUEST_CODE = 1
        private const val YOUTUBE_API_KEY = "AIzaSyAHl2dCBYq39cjJmi-o1lS1vH63vRdNNWg"

        fun start(context: Context, trailerId: String) {
            val intent = Intent(context, YouTubeActivity::class.java)
                    .putExtra(EXTRA_TRAILER_ID, trailerId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube)

        playerView = findViewById(R.id.player)
        playerView.initialize(YOUTUBE_API_KEY, this)
    }


    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOVERY_DIALOG_REQUEST_CODE) {
            playerView.initialize(YOUTUBE_API_KEY, this)
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        stylePlayer(player)
        if (!wasRestored) {
            player.loadVideo(intent.getStringExtra(EXTRA_TRAILER_ID))
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

    private fun stylePlayer(player: YouTubePlayer) {
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
        var controlFlags = player.fullscreenControlFlags
        controlFlags = controlFlags or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
        player.fullscreenControlFlags = controlFlags
    }

}