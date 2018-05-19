package com.markus.subscity.ui.splash

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.ui.cinema.CinemaActivity
import com.markus.subscity.ui.city.FirstPickCityActivity
import com.markus.subscity.ui.deeplink.DeepLinkPresenter
import com.markus.subscity.ui.deeplink.DeepLinkView
import com.markus.subscity.ui.deeplink.isFromDeepLink
import com.markus.subscity.ui.main.MainActivity
import com.markus.subscity.ui.movie.MovieActivity

/**
 * @author Vitaliy Markus
 */
class SplashActivity : MvpAppCompatActivity(), SplashView, DeepLinkView {

    @InjectPresenter
    lateinit var splashPresenter: SplashPresenter

    @InjectPresenter
    lateinit var deepLinkPresenter: DeepLinkPresenter

    @ProvidePresenter
    fun splashPresenter(): SplashPresenter {
        return SubsCityDagger.component.createSplashPresenter()
    }

    @ProvidePresenter
    fun deepLinkPresenter(): DeepLinkPresenter {
        return SubsCityDagger.component.createDeepLinkPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isFromDeepLink) {
            deepLinkPresenter.performDeepLink(intent.data)
        } else {
            splashPresenter.checkFirstLaunch()
        }
    }

    override fun showMain() {
        MainActivity.start(this)
        finish()
    }

    override fun showMovies() {
        analytics().logOpenMovies(true)
        MainActivity.start(this, MainActivity.Companion.Mode.MOVIES)
        finish()
    }

    override fun showCinemas() {
        analytics().logOpenCinemas(true)
        MainActivity.start(this, MainActivity.Companion.Mode.CINEMAS)
        finish()
    }

    override fun showMovie(movieId: Long) {
        analytics().logOpenMovie(movieId, null, true)
        MovieActivity.start(this, movieId)
//        TaskStackBuilder.create(this)
//                .addNextIntent(MainActivity.createIntent(this, MainActivity.Companion.Mode.MOVIES))
//                .addNextIntent(MovieActivity.createIntent(this, movieId))
//                .startActivities()
        //startActivities(arrayOf(MainActivity.createIntent(this, MainActivity.Companion.Mode.MOVIES), MovieActivity.createIntent(this, movieId)))
        finish()
    }

    override fun showCinema(cinemaId: Long) {
        analytics().logOpenCinema(cinemaId, null, true)
        CinemaActivity.start(this, cinemaId)
        finish()
    }

    override fun showCityPicker() {
        FirstPickCityActivity.start(this)
        finish()
    }
}