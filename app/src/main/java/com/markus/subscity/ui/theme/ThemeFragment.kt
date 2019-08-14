package com.markus.subscity.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.setSupportActionBar
import com.markus.subscity.providers.ThemeProvider
import com.markus.subscity.ui.main.MainActivity

/**
 * @author Vitaliy Markus
 */
class ThemeFragment : MvpAppCompatFragment(), ThemeView {

    @InjectPresenter
    lateinit var themePresenter: ThemePresenter

    private lateinit var themeList: RecyclerView

    companion object {
        fun newInstance(): ThemeFragment {
            return ThemeFragment()
        }
    }

    @ProvidePresenter
    fun themePresenter(): ThemePresenter {
        return SubsCityDagger.component.createThemePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_theme, container, false)
        themeList = root.findViewById(R.id.rv_list)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun showThemes(list: List<ThemeProvider.SelectedThemeItem>) {
        themeList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ThemeAdapter(requireActivity(), list, ::updateTheme)
        }
    }

    private fun updateTheme(item: ThemeProvider.SelectedThemeItem) {
        themePresenter.updateTheme(item)
    }

    override fun recreate() {
        val activity = requireActivity()
        val bundle = ActivityOptionsCompat.makeCustomAnimation(activity, 0, 0).toBundle()
        TaskStackBuilder.create(activity)
                .addNextIntent(MainActivity.createIntent(activity, MainActivity.Companion.Mode.SETTINGS))
                .addNextIntent(ThemeActivity.createIntent(activity, changingTheme = false))
                .startActivities(bundle)
    }
}