package com.markus.subscity

import com.markus.subscity.db.converters.Converter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * @author Vitaliy Markus
 */
class DbConverterTest {

    @Test
    fun `check list of long`() {
        val expectedList = LongArray(10) { Random.nextLong() }.toList()
        val dbValue = Converter.convertLongListToString(expectedList)
        val actualList = Converter.convertStringToLongList(dbValue)
        assertTrue(expectedList == actualList)
    }

    @Test
    fun `check list of string`() {
        val expectedList = LongArray(10) { Random.nextLong() }.map { it.toString() }.toList()
        val dbValue = Converter.convertStringListToString(expectedList)
        val actualList = Converter.convertStringToStringList(dbValue)
        assertTrue(expectedList == actualList)
    }

    @Test
    fun `check date time`() {
        val expectedDateTime = DateTime.now()
        val dbValue = Converter.convertDateToLong(expectedDateTime)
        val actualDateTime = Converter.convertLongToDate(dbValue)
        assertTrue(expectedDateTime == actualDateTime)
    }

    @Test
    fun aaaaa(){
        Observable.timer(10, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .map {
                    println("mapThread = ${Thread.currentThread().name}")
                }
                .doOnSubscribe {
                    println("onSubscribeThread = ${Thread.currentThread().name}")
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.single())
                .flatMap {
                    println("flatMapThread = ${Thread.currentThread().name}")
                    Observable.just(it)
                            .subscribeOn(Schedulers.computation())
                }
                .subscribe {
                    println("subscribeThread = ${Thread.currentThread().name}")
                }
        Thread.sleep(5000)
    }
}