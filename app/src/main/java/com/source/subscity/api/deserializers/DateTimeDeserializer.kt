package com.source.subscity.api.deserializers

import com.google.gson.*
import org.joda.time.DateTime
import java.lang.reflect.Type

/**
 * @author Vitaliy Markus
 */
object DateTimeDeserializer : JsonDeserializer<DateTime> {

    val emptyDateTime: DateTime by lazy { DateTime(0) }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime {
        return if (json != null) DateTime(json.asString) else emptyDateTime
    }

}