package com.markus.subscity.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.extensions.rateApp
import com.markus.subscity.ui.main.MainActivity.Companion.Mode.*


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    private lateinit var dialogRateBehavior: BottomSheetBehavior<View>
    private lateinit var viewPager: ViewPager
    private lateinit var ahBottomView: AHBottomNavigation

    companion object {

        private const val EXTRA_MODE = "mode"
        private const val DELAY = 3000L

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

    @ProvidePresenter
    fun mainPresenter(): MainPresenter {
        return SubsCityDagger.component.createMainPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.ahb_pager)
        ahBottomView = findViewById(R.id.ahb_bottom)
        styleViewPager()
        styleBottomNavigation()
        initRateDialog()
        supportActionBar!!.setTitle(R.string.main_films)
        analytics().logOpenMain()
    }

    override fun showRateDialog() {
        analytics().logOpenRateDialog()
        viewPager.postDelayed({
            dialogRateBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }, DELAY)
    }

    override fun openPlayStore() {
        dialogRateBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        rateApp()
    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }

    override fun onBackPressed() {
        if (dialogRateBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            dialogRateBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            super.onBackPressed()
        }
    }

    private fun styleViewPager() {
        viewPager.adapter = MainFragmentAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = 2
    }

    private fun styleBottomNavigation() {
        val adapter = AHBottomNavigationAdapter(this, R.menu.menu_main)
        adapter.setupWithBottomNavigation(ahBottomView)

        ahBottomView.isBehaviorTranslationEnabled = false
        ahBottomView.accentColor = ContextCompat.getColor(this, R.color.bottom_bar_active_color)
        ahBottomView.inactiveColor = ContextCompat.getColor(this, R.color.bottom_bar_inactive_color)
        ahBottomView.defaultBackgroundColor = ContextCompat.getColor(this, R.color.bottom_bar_background_color)
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

    private fun initRateDialog() {
        val dialogRate = findViewById<View>(R.id.dialog_rate)
        dialogRate.setOnClickListener {}
        dialogRateBehavior = BottomSheetBehavior.from(dialogRate)
        dialogRateBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        findViewById<View>(R.id.bt_rate_app).setOnClickListener { mainPresenter.openPlayStore() }
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
