package com.source.subscity.ui.main

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.source.subscity.R
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.ui.cinema.CinemaActivity
import com.source.subscity.ui.deeplink.DeepLinkPresenter
import com.source.subscity.ui.deeplink.DeepLinkView
import com.source.subscity.ui.deeplink.isFromDeepLink
import com.source.subscity.ui.movie.MovieActivity

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

        if (isFromDeepLink) {
            deepLinkPresenter.performDeepLink(intent.data)
        }
    }

    override fun showMain() {
        showMovies()
    }

    override fun showMovies() {
        ahBottomView.currentItem = 0
    }

    override fun showCinemas() {
        ahBottomView.currentItem = 1
    }

    override fun showMovie(movieId: Long) {
        showMovies()
        MovieActivity.start(this, movieId)
    }

    override fun showCinema(cinemaId: Long) {
        showCinemas()
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
        ahBottomView.setOnTabSelectedListener { position, wasSelected -> viewPager.setCurrentItem(position, false); true }
        ahBottomView.setUseElevation(true)
    }

}
