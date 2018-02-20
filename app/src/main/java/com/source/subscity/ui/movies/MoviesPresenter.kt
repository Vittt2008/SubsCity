package com.source.subscity.ui.movies

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.api.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MoviesPresenter @Inject constructor(private val apiClient: ApiClient) : MvpPresenter<MoviesView>() {

    override fun onFirstViewAttach() {
        apiClient.subsCityService.getMovies("spb")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showMovies(it) },
                        { }
                )
    }
}
