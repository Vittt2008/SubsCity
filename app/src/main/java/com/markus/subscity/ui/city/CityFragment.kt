package com.markus.subscity.ui.city

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
import com.markus.subscity.extensions.setSupportActionBar
import com.markus.subscity.extensions.toast

/**
 * @author Vitaliy Markus
 */
class CityFragment : MvpAppCompatFragment(), CityView {

    @InjectPresenter
    lateinit var cityPresenter: CityPresenter

    private lateinit var cityList: RecyclerView

    companion object {
        fun newInstance(): CityFragment {
            return CityFragment()
        }
    }

    @ProvidePresenter
    fun cityPresenter(): CityPresenter {
        return SubsCityDagger.component.createCityPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_city, container, false)
        cityList = root.findViewById(R.id.rv_list)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun showCities(cities: List<City>, currentCity: String) {
        cityList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CityAdapter(activity!!, cities, currentCity, ::updateCity)
        }
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

    private fun updateCity(city: String) {
        analytics().logSwitchCity(city)
        cityPresenter.updateCity(city)
    }

}