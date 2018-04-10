package com.markus.subscity.ui.splash

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.ui.city.CityActivity
import com.markus.subscity.ui.main.MainActivity

/**
 * @author Vitaliy Markus
 */
class SplashActivity : MvpAppCompatActivity(), SplashView {


    @InjectPresenter
    lateinit var splashPresenter: SplashPresenter


    @ProvidePresenter
    fun splashPresenter(): SplashPresenter {
        return SubsCityDagger.component.createSplashPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashPresenter.checkFirstLaunch()
    }

    override fun showMain() {
        MainActivity.start(this)
        finish()
    }

    override fun showCityPicker() {
        CityActivity.start(this, true)
        finish()
    }
}