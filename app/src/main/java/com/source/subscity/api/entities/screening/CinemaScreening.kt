package com.source.subscity.api.entities.screening

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import com.source.subscity.db.converters.Converter
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@Entity
@TypeConverters(Converter::class)
data class CinemaScreening(@SerializedName("movie_id") val movieId: Long,
                           @SerializedName("date_time") val dateTime: DateTime,
                           @SerializedName("price_max") val priceMax: Int,
                           @SerializedName("price_min") val priceMin: Int,
                           @SerializedName("screening_id") @PrimaryKey val screeningId: Long) : Serializable {

    @Ignore
    constructor() : this(0, DateTime(0), 0, 0, 0)
}