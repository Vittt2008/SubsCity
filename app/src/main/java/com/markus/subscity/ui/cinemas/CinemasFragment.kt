package com.markus.subscity.ui.cinemas

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.api.entities.City
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.controllers.ContentLoadingController
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.supportActionBar
import com.markus.subscity.ui.cinema.CinemaActivity
import com.markus.subscity.ui.cinemasmap.CinemasMapActivity
import com.markus.subscity.widgets.divider.MarginDivider

/**
 * @author Vitaliy Markus
 */
class CinemasFragment : MvpAppCompatFragment(), CinemasView {

    private lateinit var loadingController: ContentLoadingController

    @InjectPresenter
    lateinit var cinemasPresenter: CinemasPresenter

    private lateinit var cinemasList: RecyclerView

    companion object {
        fun newInstance() = CinemasFragment()
    }

    @ProvidePresenter
    fun cinemasPresenter(): CinemasPresenter {
        return SubsCityDagger.component.createCinemasPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_cinemas, container, false)
        cinemasList = root.findViewById(R.id.rv_list)
        loadingController = ContentLoadingController(root, R.id.rv_list, R.id.pb_progress)
        return root
    }

    override fun showCinemas(cinemas: List<Cinema>) {
        cinemas.toString().equals("", true)
        cinemasList.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = CinemasAdapter(cinemas) { CinemaActivity.start(activity!!, it.id) }
            addItemDecoration(MarginDivider(activity!!).apply { setDrawable(R.drawable.cinema_divider) })
        }
    }

    override fun showCinemasMap(city: City) {
        CinemasMapActivity.start(activity!!, city.location.latitude, city.location.longitude, city.location.zoom)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cinema, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_map_cinema) {
            cinemasPresenter.showCinemasMap()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            activity?.let { it.supportActionBar.setTitle(R.string.main_cinemas) }
        }
    }


    override fun onError(throwable: Throwable) {
        Toast.makeText(activity, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        loadingController.switchState(ContentLoadingController.State.PROGRESS)
    }

    override fun hideProgress() {
        loadingController.switchState(ContentLoadingController.State.CONTENT)
    }

}