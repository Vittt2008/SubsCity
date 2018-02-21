package com.source.subscity.api.entities.movie

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
data class Movie(@SerializedName("id") @PrimaryKey val id: Long,
                 @SerializedName("age_restriction") val ageRestriction: Int,
                 @SerializedName("cast") val cast: List<String>,
                 @SerializedName("countries") val countries: List<String>,
                 @SerializedName("created_at") val createdAt: DateTime,
                 @SerializedName("description") val description: String,
                 @SerializedName("directors") val directors: List<String>,
                 @SerializedName("duration") val duration: Int,
                 @SerializedName("genres") val genres: List<String>,
                 @SerializedName("languages") val languages: List<String>,
                 @SerializedName("poster") val poster: String,
                 @SerializedName("rating") @Embedded(prefix = "rating_") val rating: Ratings,
                 @SerializedName("screenings") @Embedded(prefix = "screenings_") val screenings: Screenings,
                 @SerializedName("title") @Embedded(prefix = "title_") val title: Titles,
                 @SerializedName("trailer") @Embedded(prefix = "trailer_") val trailer: Trailers,
                 @SerializedName("year") val year: Int) : Serializable {

    @Ignore
    constructor() : this(0, 0, emptyList(), emptyList(), DateTime(0), "",
            emptyList(), 0, emptyList(), emptyList(), "", Ratings(), Screenings(),
            Titles(), Trailers(), 0)

    val commonRating: Double
        get() {
            val sum = rating.imdb.rating + rating.kinopoisk.rating
            val count = if (rating.imdb.rating != 0.0 && rating.kinopoisk.rating != 0.0) 2 else 1
            return sum / count
        }
}