package com.markus.subscity

import com.markus.subscity.providers.DateTimeProvider
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
class TestDateTimeProvider : DateTimeProvider {

    var now: DateTime = DateTime.now()

    override fun now(): DateTime {
        return now
    }
}