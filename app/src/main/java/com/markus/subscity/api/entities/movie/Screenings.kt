package com.markus.subscity.api.entities.movie

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
data class Screenings(@SerializedName("count") val count: Int,
                      @SerializedName("next") val next: DateTime) {

    @Ignore
    constructor() : this(0, DateTime(0))
}