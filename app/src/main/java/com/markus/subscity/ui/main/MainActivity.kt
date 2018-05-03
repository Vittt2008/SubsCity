package com.markus.subscity.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.ui.main.MainActivity.Companion.Mode.*
import android.support.design.widget.BottomSheetBehavior


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var ahBottomView: AHBottomNavigation

    companion object {

        private const val EXTRA_MODE = "mode"

        fun start(context: Context) {
            start(context, MOVIES)
        }

        fun start(context: Context, mode: Mode) {
            val intent = Intent(context, MainActivity::class.java)
                    .putExtra(EXTRA_MODE, mode)
            context.startActivity(intent)
        }

        fun createIntent(context: Context, mode: Mode): Intent {
            return Intent(context, MainActivity::class.java)
                    .putExtra(EXTRA_MODE, mode)
        }

        enum class Mode {
            MOVIES,
            CINEMAS,
            SETTINGS
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.ahb_pager)
        ahBottomView = findViewById(R.id.ahb_bottom)
        styleViewPager()
        styleBottomNavigation()
        supportActionBar!!.setTitle(R.string.main_films)
        analytics().logOpenMain()

        val appBar = findViewById<View>(R.id.dialog_rate)
        appBar.setOnClickListener{}
        val bottomSheetBehavior = BottomSheetBehavior.from(appBar)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewPager.postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
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

        val mode = intent.getSerializableExtra(EXTRA_MODE) as Mode
        when (mode) {
            MOVIES -> ahBottomView.currentItem = 0
            CINEMAS -> ahBottomView.currentItem = 1
            SETTINGS -> ahBottomView.currentItem = 2
        }
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
