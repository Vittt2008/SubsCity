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
import com.markus.subscity.extensions.supportActionBar
import com.markus.subscity.ui.about.AboutActivity
import com.markus.subscity.ui.cinemasmap.CinemasMapActivity
import com.markus.subscity.ui.city.CityActivity
import com.markus.subscity.ui.donate.DonateActivity

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

    override fun showSettings(city: String) {
        settingsList.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = SettingsAdapter(city) { item ->
                when (item) {
                //SettingsAdapter.SOON_AT_BOX_OFFICE -> {}
                    SettingsAdapter.CINEMA_MAP -> settingsPresenter.showCinemasMap()
                //SettingsAdapter.SALES -> {}
                    SettingsAdapter.ABOUT -> AboutActivity.start(activity!!)
                    SettingsAdapter.DONATE -> DonateActivity.start(activity!!)
                    SettingsAdapter.CITY -> CityActivity.start(activity!!)
                }
            }
        }
    }

    override fun showCinemasMap(city: City) {
        CinemasMapActivity.start(activity!!, city.location.latitude, city.location.longitude, city.location.zoom)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            activity?.let { it.supportActionBar.setTitle(R.string.main_settings) }
        }
    }
}