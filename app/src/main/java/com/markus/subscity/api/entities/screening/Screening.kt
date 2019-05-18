package com.markus.subscity.api.entities.screening

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.markus.subscity.db.converters.Converter
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@Entity
@TypeConverters(Converter::class)
data class Screening(@SerializedName("id") @PrimaryKey val id: Long,
                     @SerializedName("tickets_url") val ticketsUrl: String,
                     @SerializedName("movie_id") val movieId: Long,
                     @SerializedName("cinema_id") val cinemaId: Long,
                     @SerializedName("date_time") val dateTime: DateTime,
                     @SerializedName("price_max") val priceMax: Int,
                     @SerializedName("price_min") val priceMin: Int) : Serializable {

    @Ignore
    constructor() : this(0, "", 0, 0, DateTime(0), 0, 0)
}