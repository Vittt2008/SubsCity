package com.markus.subscity.ui.cinemas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.markus.subscity.api.entities.City
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.repositories.CinemaRepository
import com.markus.subscity.viewmodels.BaseViewModel
import com.markus.subscity.viewmodels.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CinemasViewModel @Inject constructor(private val cinemaRepository: CinemaRepository,
                                           private val cityProvider: CityProvider) : BaseViewModel() {

    private val progressInner = MutableLiveData<Boolean>()
    private val cinemasInner = MutableLiveData<List<Cinema>>()
    private val mapEventInner = LiveEvent<City>()

    val progress: LiveData<Boolean>
        get() = progressInner

    val cinemas: LiveData<List<Cinema>>
        get() = cinemasInner

    val mapEvent: LiveData<City>
        get() = mapEventInner

    init {
        progressInner.postValue(true)
        cityProvider.asyncCity
                .flatMapSingle { cinemaRepository.getCinemas() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeTillDetach({ cinemas ->
                    progressInner.postValue(false)
                    cinemasInner.postValue(cinemas)
                }, {
                    progressInner.postValue(false)
                    errorEventInner.postValue(it)
                })
    }

    fun showCinemasMap() {
        mapEventInner.postValue(cityProvider.city)
    }
}