package com.markus.subscity.ui.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.api.entities.City
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.extensions.rateApp
import com.markus.subscity.extensions.supportActionBar
import com.markus.subscity.ui.about.AboutActivity
import com.markus.subscity.ui.cinemasmap.CinemasMapActivity
import com.markus.subscity.ui.city.CityActivity
import com.markus.subscity.ui.donate.DonateActivity
import com.markus.subscity.ui.policy.PolicyActivity
import com.markus.subscity.ui.settings.SettingsView.Companion.ABOUT
import com.markus.subscity.ui.settings.SettingsView.Companion.CINEMA_MAP
import com.markus.subscity.ui.settings.SettingsView.Companion.CITY
import com.markus.subscity.ui.settings.SettingsView.Companion.DONATE
import com.markus.subscity.ui.settings.SettingsView.Companion.POLICY
import com.markus.subscity.ui.settings.SettingsView.Companion.RATE_APP

/**
 * @author Vitaliy Markus
 */
class SettingsFragment : MvpAppCompatFragment(), SettingsView {

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    private lateinit var settingsList: RecyclerView

    companion object {
        fun newInstance() = SettingsFragment()
    }

    @ProvidePresenter
    fun moviesPresenter(): SettingsPresenter {
        return SubsCityDagger.component.createSettingsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsList = inflater.inflate(R.layout.fragment_settings, container, false) as RecyclerView
        return settingsList
    }

    override fun showSettings(settings: List<SettingsView.SettingItem>) {
        settingsList.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = SettingsAdapter(settings) { item ->
                when (item) {
                    CINEMA_MAP -> settingsPresenter.showCinemasMap()
                    RATE_APP -> settingsPresenter.openPlayStore()
                    ABOUT -> openAbout()
                    POLICY -> openPolicy()
                    DONATE -> DonateActivity.start(activity!!)
                    CITY -> openCityPicker()
                }
            }
        }
    }

    override fun showCinemasMap(city: City) {
        analytics().logOpenCinemasMap(city.name, false)
        CinemasMapActivity.start(activity!!, city.location.latitude, city.location.longitude, city.location.zoom)
    }

    override fun openPlayStore() {
        rateApp()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            activity?.let { it.supportActionBar.setTitle(R.string.main_settings) }
        }
    }

    private fun openAbout() {
        analytics().logOpenAbout()
        AboutActivity.start(activity!!)
    }

    private fun openPolicy() {
        analytics().logOpenPolicy()
        PolicyActivity.start(activity!!)
    }

    private fun openCityPicker() {
        analytics().logOpenCityPicker()
        CityActivity.start(activity!!)
    }
}