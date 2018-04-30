package com.markus.subscity.providers

import org.joda.time.DateTime
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
interface DateTimeProvider {
    fun now(): DateTime
}

class DateTimeProviderImpl @Inject constructor() : DateTimeProvider {

    override fun now(): DateTime {
        return DateTime.now()
    }
}