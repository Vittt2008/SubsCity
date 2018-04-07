package com.markus.subscity.ui.city

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
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
import com.markus.subscity.extensions.setSupportActionBar
import com.markus.subscity.extensions.setSupportActionBarWithoutBackButton
import com.markus.subscity.extensions.toast
import com.markus.subscity.ui.main.MainActivity

/**
 * @author Vitaliy Markus
 */
class CityFragment : MvpAppCompatFragment(), CityView {

    @InjectPresenter
    lateinit var cityPresenter: CityPresenter

    private var isFirstPick = false

    private lateinit var cityList: RecyclerView

    companion object {
        fun newInstance(firstPick: Boolean): CityFragment {
            return CityFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(CityActivity.EXTRA_FIRST_PICK, firstPick)
                }
            }
        }
    }

    @ProvidePresenter
    fun cityPresenter(): CityPresenter {
        return SubsCityDagger.component.createCityPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_city, container, false)
        isFirstPick = arguments?.getBoolean(CityActivity.EXTRA_FIRST_PICK, false) ?: false
        cityList = root.findViewById(R.id.rv_list)
        initToolbar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun showCities(cities: List<City>, currentCity: String) {
        cityList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CityAdapter(activity!!, cities, if (isFirstPick) null else currentCity, ::updateCity)
        }
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

    private fun updateCity(city: String) {
        analytics().logSwitchCity(city)
        cityPresenter.updateCity(city)
        if (isFirstPick) {
            MainActivity.start(activity!!)
            activity!!.finish()
        }
    }

    private fun initToolbar(toolbar: Toolbar) {
        if (isFirstPick) {
            setSupportActionBarWithoutBackButton(toolbar)
        } else {
            setSupportActionBar(toolbar)
        }
    }


}