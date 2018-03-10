package com.source.subscity.ui.city

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
import com.source.subscity.extensions.setSupportActionBar
import com.source.subscity.extensions.toast

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
        activity!!.setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun showCities(cities: List<City>) {
        cityList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CityAdapter(activity!!, cities) { cityPresenter.updateCity(it) }
        }
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

}