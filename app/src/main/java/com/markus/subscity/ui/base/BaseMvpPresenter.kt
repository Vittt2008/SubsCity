package com.markus.subscity.ui.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

open class BaseMvpPresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
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