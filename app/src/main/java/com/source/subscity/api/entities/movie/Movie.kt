package com.source.subscity.api.entities.movie

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Movie(@SerializedName("id") val id: Long = 0,
                 @SerializedName("age_restriction") val ageRestriction: Int = 0,
                 @SerializedName("cast") val cast: List<String> = emptyList(),
                 @SerializedName("countries") val countries: List<String> = emptyList(),
                 @SerializedName("created_at") val createdAt: DateTime = DateTime(0),
                 @SerializedName("description") val description: String,
                 @SerializedName("directors") val directors: List<String> = emptyList(),
                 @SerializedName("duration") val duration: Int = 0,
                 @SerializedName("genres") val genres: List<String> = emptyList(),
                 @SerializedName("languages") val languages: List<String> = emptyList(),
                 @SerializedName("poster") val poster: String = "",
                 @SerializedName("rating") val rating: Ratings = Ratings(),
                 @SerializedName("screenings") val screenings: Screenings = Screenings(),
                 @SerializedName("title") val title: Titles = Titles(),
                 @SerializedName("trailer") val trailer: Trailers = Trailers(),
                 @SerializedName("year") val year: Int = 0) : Serializable