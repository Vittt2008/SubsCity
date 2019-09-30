package com.markus.subscity

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.markus.subscity.helper.createCachedRepository
import com.markus.subscity.helper.createDateTimeProvider
import com.markus.subscity.impl.TestCachedRepository.Companion.FROM_DB
import com.markus.subscity.impl.TestCachedRepository.Companion.FROM_SERVER
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Vitaliy Markus
 */
@RunWith(AndroidJUnit4::class)
class CachedRepositoryTest {

    companion object {
        private val periodParser = PeriodFormatterBuilder().appendDays().appendLiteral("d ").appendHours().appendLiteral(":").appendMinutes().toFormatter()
        private val syncTime = arrayOf("05:55", "10:05", "14:05", "19:00", "23:00")
        private val requestTime = arrayOf(
                "0d 00:00", "0d 03:00", "0d 05:00", "0d 06:00", "0d 09:00",
                "0d 10:00", "0d 12:00", "0d 18:00", "0d 19:00", "0d 19:05",
                "0d 20:00", "0d 22:00", "1d 03:00", "1d 04:00", "1d 06:00"
        )
        private val expectedResult = arrayOf(
                FROM_SERVER, FROM_DB, FROM_DB, FROM_SERVER, FROM_DB,
                FROM_DB, FROM_SERVER, FROM_SERVER, FROM_DB, FROM_SERVER,
                FROM_DB, FROM_DB, FROM_SERVER, FROM_DB, FROM_DB
        )
    }

    @Test
    fun checkCachedRepository() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        JodaTimeAndroid.init(appContext)

        val dateTimeProvider = createDateTimeProvider()
        val cachedRepository = createCachedRepository(appContext, dateTimeProvider)
        val resultList = mutableListOf<String>()

        cachedRepository.testSyncTime = syncTime
        for (time in requestTime) {
            val now = calculateCurrentTime(time)
            dateTimeProvider.now = now
            val data = cachedRepository.getData()
            resultList.add(data)
        }

        val actualResult = resultList.toTypedArray()
        assertTrue(actualResult.contentEquals(expectedResult))
    }

    private fun calculateCurrentTime(it: String): DateTime {
        val now = DateTime.now().withTimeAtStartOfDay()
        return now.plus(Period.parse(it, periodParser))
    }
}
