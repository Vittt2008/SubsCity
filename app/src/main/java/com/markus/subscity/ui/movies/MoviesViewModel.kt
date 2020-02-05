package com.markus.subscity.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.markus.subscity.viewmodels.BaseViewModel
import javax.inject.Inject

class MoviesViewModel @Inject constructor() : BaseViewModel() {

    private val progressInner = MutableLiveData<Boolean>()

    val progress: LiveData<Boolean>
        get() = progressInner

    init {
        progressInner.postValue(true)
    }
}