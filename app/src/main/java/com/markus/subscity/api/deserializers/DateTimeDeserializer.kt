package com.markus.subscity.api.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type

/**
 * @author Vitaliy Markus
 */
object DateTimeDeserializer : JsonDeserializer<DateTime> {

    private val emptyDateTime: DateTime by lazy { DateTime(0) }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime {
        return if (json != null) ISODateTimeFormat.dateTimeParser().parseDateTime(json.asString) else emptyDateTime
    }

}