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
import com.source.subscity.dagger.SubsCityDagger

/**
 * @author Vitaliy Markus
 */
class SettingsFragment : MvpAppCompatFragment(), SettingsView {

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    private lateinit var settingsList: RecyclerView

    @ProvidePresenter
    fun moviesPresenter(): SettingsPresenter {
        return SubsCityDagger.component.createSettingsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsList = inflater.inflate(R.layout.fragment_settings, container, false) as RecyclerView
        return settingsList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        settingsList.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = SettingsAdapter()
        }
    }
}