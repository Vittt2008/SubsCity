package com.markus.subscity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    protected val errorEventInner = LiveEvent<Throwable>()

    val errorEvent: LiveData<Throwable>
        get() = errorEventInner

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun <T> Observable<T>.subscribeTillDetach(onNext: (T) -> Unit = {}): Disposable = subscribeTillDetach(onNext, {})

    protected fun <T> Observable<T>.subscribeTillDetach(onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        val disposable = subscribe(onNext, onError)
        compositeDisposable += disposable
        return disposable
    }

    protected fun <T> Single<T>.subscribeTillDetach(onNext: (T) -> Unit = {}): Disposable = subscribeTillDetach(onNext, {})

    protected fun <T> Single<T>.subscribeTillDetach(onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        val disposable = subscribe(onNext, onError)
        compositeDisposable += disposable
        return disposable
    }
}