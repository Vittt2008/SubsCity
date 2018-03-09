package com.source.subscity.ui.cinemas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.source.subscity.R
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.extensions.supportActionBar
import com.source.subscity.widgets.divider.MarginDivider

/**
 * @author Vitaliy Markus
 */
class CinemasFragment : MvpAppCompatFragment(), CinemasView {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cinemasList = inflater.inflate(R.layout.fragment_cinemas, container, false) as RecyclerView
        return cinemasList
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            activity?.let { it.supportActionBar.setTitle(R.string.main_cinemas) }
        }
    }

    override fun showCinemas(cinemas: List<Cinema>) {
        cinemas.toString().equals("", true)
        cinemasList.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = CinemasAdapter(cinemas)
            addItemDecoration(MarginDivider(activity!!).apply { setDrawable(R.drawable.cinema_divider) })
        }
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(activity, throwable.message, Toast.LENGTH_SHORT).show()
    }


}