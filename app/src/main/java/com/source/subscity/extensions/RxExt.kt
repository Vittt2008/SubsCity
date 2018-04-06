package com.source.subscity.extensions

import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * @author Vitaliy Markus
 */
const val RX_TIMEOUT = 1L

inline fun <reified T> Single<T>.timeout(): Single<T> {
    return this.timeout(RX_TIMEOUT, TimeUnit.SECONDS)
    //return this
}

inline fun <reified T> Observable<T>.timeout(): Observable<T> {
    return this.timeout(RX_TIMEOUT, TimeUnit.SECONDS)
}

inline fun <reified T> Single<List<T>>.checkIsEmpty(): Single<List<T>> {
    //return this.flatMap { if (it.isNotEmpty()) Single.just(it) else Single.error(kotlin.IllegalArgumentException("Collection is empty")) }
    return this.map { if (it.isNotEmpty()) it else throw IllegalArgumentException("Collection is empty") }
}

inline fun <reified T> Observable<List<T>>.checkIsEmpty(): Observable<List<T>> {
    //return this.flatMap { if (it.isNotEmpty()) Observable.just(it) else Observable.error(kotlin.IllegalArgumentException("Collection is empty")) }
    return this.map { if (it.isNotEmpty()) it else throw IllegalArgumentException("Collection is empty") }
}
