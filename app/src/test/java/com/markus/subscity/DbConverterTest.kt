package com.markus.subscity

import com.markus.subscity.db.converters.Converter
import org.joda.time.DateTime
import org.junit.Assert.assertTrue
import org.junit.Test
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
}