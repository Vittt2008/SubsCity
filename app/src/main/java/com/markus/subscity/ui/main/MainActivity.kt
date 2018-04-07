package com.markus.subscity.ui.main

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.ui.cinema.CinemaActivity
import com.markus.subscity.ui.deeplink.DeepLinkPresenter
import com.markus.subscity.ui.deeplink.DeepLinkView
import com.markus.subscity.ui.deeplink.isFromDeepLink
import com.markus.subscity.ui.movie.MovieActivity

class MainActivity : MvpAppCompatActivity(), DeepLinkView {

    private lateinit var viewPager: ViewPager
    private lateinit var ahBottomView: AHBottomNavigation

    @InjectPresenter
    lateinit var deepLinkPresenter: DeepLinkPresenter

    @ProvidePresenter
    fun deepLinkPresenter(): DeepLinkPresenter {
        return SubsCityDagger.component.createDeepLinkPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.ahb_pager)
        ahBottomView = findViewById(R.id.ahb_bottom)
        styleViewPager()
        styleBottomNavigation()
        supportActionBar!!.setTitle(R.string.main_films)

        if (isFromDeepLink) {
            deepLinkPresenter.performDeepLink(intent.data)
        }

        analytics().logOpenMain()
    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }

    override fun showMain() {
        ahBottomView.currentItem = 0
    }

    override fun showMovies() {
        ahBottomView.currentItem = 0
        analytics().logOpenMovies(true)
    }

    override fun showCinemas() {
        ahBottomView.currentItem = 1
        analytics().logOpenCinemas(true)
    }

    override fun showMovie(movieId: Long) {
        ahBottomView.currentItem = 0
        analytics().logOpenMovie(movieId, null,true)
        MovieActivity.start(this, movieId)
    }

    override fun showCinema(cinemaId: Long) {
        ahBottomView.currentItem = 1
        analytics().logOpenCinema(cinemaId, null, true)
        CinemaActivity.start(this, cinemaId)
    }

    private fun styleViewPager() {
        viewPager.adapter = MainFragmentAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = 2
    }

    private fun styleBottomNavigation() {
        val adapter = AHBottomNavigationAdapter(this, R.menu.menu_main)
        adapter.setupWithBottomNavigation(ahBottomView)

        ahBottomView.isBehaviorTranslationEnabled = false
        ahBottomView.accentColor = ContextCompat.getColor(this, R.color.primary_color)
        ahBottomView.inactiveColor = ContextCompat.getColor(this, R.color.inactive_color)
        ahBottomView.defaultBackgroundColor = ContextCompat.getColor(this, R.color.white_color)
        ahBottomView.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        ahBottomView.setOnTabSelectedListener { position, wasSelected -> selectTab(position) }
        ahBottomView.setUseElevation(true)
    }

    private fun selectTab(position: Int): Boolean {
        viewPager.setCurrentItem(position, false)
        when (position) {
            0 -> analytics().logOpenMovies(false)
            1 -> analytics().logOpenCinemas(false)
            else -> analytics().logOpenSettings()
        }
        return true
    }

}
