package com.markus.subscity.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.api.entities.City
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.extensions.rateApp
import com.markus.subscity.extensions.supportActionBar
import com.markus.subscity.providers.ThemeProvider
import com.markus.subscity.ui.about.AboutActivity
import com.markus.subscity.ui.cinemasmap.CinemasMapActivity
import com.markus.subscity.ui.city.CityActivity
import com.markus.subscity.ui.donate.DonateActivity
import com.markus.subscity.ui.main.MainActivity
import com.markus.subscity.ui.policy.PolicyActivity
import com.markus.subscity.ui.settings.SettingsView.Companion.ABOUT
import com.markus.subscity.ui.settings.SettingsView.Companion.CINEMA_MAP
import com.markus.subscity.ui.settings.SettingsView.Companion.CITY
import com.markus.subscity.ui.settings.SettingsView.Companion.DIALOG_THEME
import com.markus.subscity.ui.settings.SettingsView.Companion.DONATE
import com.markus.subscity.ui.settings.SettingsView.Companion.LANGUAGE
import com.markus.subscity.ui.settings.SettingsView.Companion.POLICY
import com.markus.subscity.ui.settings.SettingsView.Companion.RATE_APP
import com.markus.subscity.ui.settings.SettingsView.Companion.THEME
import com.markus.subscity.ui.theme.ThemeActivity

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

    override fun onResume() {
        super.onResume()
        requireActivity().supportActionBar.setTitle(R.string.main_settings)
    }

    override fun showSettings(settings: List<Setting>) {
        settingsList.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = SettingsDelegatesAdapter(settings, { item ->
                when (item) {
                    CINEMA_MAP -> settingsPresenter.showCinemasMap()
                    THEME -> openThemePicker()
                    DIALOG_THEME -> settingsPresenter.openThemeDialog()
                    LANGUAGE -> openLanguagePicker()
                    RATE_APP -> settingsPresenter.openPlayStore()
                    ABOUT -> openAbout()
                    POLICY -> openPolicy()
                    DONATE -> DonateActivity.start(requireActivity())
                    CITY -> openCityPicker()
                }
            }, ::switchTheme)
        }
    }

    override fun showCinemasMap(city: City) {
        analytics().logOpenCinemasMap(city.name, false)
        CinemasMapActivity.start(requireActivity(), city.location.latitude, city.location.longitude, city.location.zoom)
    }

    override fun openPlayStore() {
        rateApp()
    }

    override fun showThemeDialog(list: List<ThemeProvider.SelectedThemeItem>) {
        val values = list.map { getString(it.title) }.toTypedArray()
        val selected = list.indexOfFirst { it.isSelected }
        AlertDialog.Builder(requireActivity())
                .setSingleChoiceItems(values, selected) { _, index -> switchTheme(list[index].mode) }
                .setCancelable(true)
                .show()
    }

    private fun switchTheme(dark: Boolean) {
        settingsPresenter.switchTheme(dark)
    }

    private fun switchTheme(mode: Int) {
        settingsPresenter.switchTheme(mode)
    }

    override fun recreate() {
        val activity = requireActivity()
        val bundle = ActivityOptionsCompat.makeCustomAnimation(activity, android.R.anim.fade_in, android.R.anim.fade_out).toBundle()
        TaskStackBuilder.create(activity)
                .addNextIntent(MainActivity.createIntent(activity, MainActivity.Companion.Mode.SETTINGS))
                .startActivities(bundle)
    }

    private fun openAbout() {
        analytics().logOpenAbout()
        AboutActivity.start(requireActivity())
    }

    private fun openPolicy() {
        analytics().logOpenPolicy()
        PolicyActivity.start(requireActivity())
    }

    private fun openCityPicker() {
        analytics().logOpenCityPicker()
        CityActivity.start(requireActivity())
    }

    private fun openThemePicker() {
        analytics().logThemePicker()
        ThemeActivity.start(requireActivity())
    }

    private fun openLanguagePicker() {
        analytics().logLanguagePicker()
    }
}