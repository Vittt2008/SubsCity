package com.source.subscity.api.entities.screening

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class MovieScreening(@SerializedName("cinema_id") val cinemaId: Long = 0,
                          @SerializedName("date_time") val dateTime: DateTime = DateTime(0),
                          @SerializedName("price_max") val priceMax: Int = 0,
                          @SerializedName("price_min") val priceMin: Int = 0,
                          @SerializedName("screening_id") val screeningId: Long = 0) : Serializable