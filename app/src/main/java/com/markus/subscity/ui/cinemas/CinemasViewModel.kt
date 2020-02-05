package com.markus.subscity.ui.cinemas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.markus.subscity.api.entities.City
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.repositories.CinemaRepository
import com.markus.subscity.viewmodels.BaseViewModel
import com.markus.subscity.viewmodels.LiveEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
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
                .toFlowable(BackpressureStrategy.DROP)
                .asFlow()
                .flatMapConcat { cinemaRepository.getCinemasFlow() }
                .flowOn(Dispatchers.IO)
                .catch {
                    progressInner.postValue(false)
                    errorEventInner.postValue(it)
                }
                .onEach { cinemas ->
                    progressInner.postValue(false)
                    cinemasInner.postValue(cinemas)
                }
                .launchIn(viewModelScope)
    }

    fun showCinemasMap() {
        mapEventInner.postValue(cityProvider.city)
    }
}