package com.source.subscity.ui.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.source.subscity.R
import com.source.subscity.api.entities.City
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.extensions.supportActionBar
import com.source.subscity.ui.about.AboutActivity
import com.source.subscity.ui.cinemasmap.CinemasMapActivity
import com.source.subscity.ui.city.CityActivity
import com.source.subscity.ui.donate.DonateActivity

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