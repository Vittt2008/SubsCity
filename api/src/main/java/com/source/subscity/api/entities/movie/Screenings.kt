package com.source.subscity.api.entities.movie

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
data class Screenings(@SerializedName("count") val count: Int = 0,
                      @SerializedName("next") val next: DateTime = DateTime(0))