package com.markus.subscity.impl

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