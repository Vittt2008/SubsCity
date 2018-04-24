package com.markus.subscity.extensions

import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * @author Vitaliy Markus
 */
const val RX_TIMEOUT = 10L

inline fun <reified T> Single<T>.timeout(): Single<T> {
    return this.timeout(RX_TIMEOUT, TimeUnit.SECONDS)
}
