package com.source.subscity.ui.main

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.source.subscity.R
import com.source.subscity.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var ahBottomView: AHBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.ahb_pager)
        ahBottomView = findViewById(R.id.ahb_bottom)
        styleViewPager()
        styleBottomNavigation()
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
    }


}
