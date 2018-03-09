package com.source.subscity.ui.cinema

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class CinemaPresenter @Inject constructor() : MvpPresenter<CinemaView>() {

    var cinemaId: Long = 0
}
