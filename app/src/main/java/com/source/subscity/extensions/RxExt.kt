package com.source.subscity.extensions

import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * @author Vitaliy Markus
 */
const val RX_TIMEOUT = 1L

inline fun <reified T> Single<T>.timeout(): Single<T> {
//    return this.timeout(RX_TIMEOUT, TimeUnit.SECONDS)
    return this
}

inline fun <reified T> Observable<T>.timeout(): Observable<T> {
    return this.timeout(RX_TIMEOUT, TimeUnit.SECONDS)
}
