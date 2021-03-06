package com.markus.subscity.api.entities.movie

import androidx.room.Embedded
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Ratings(@SerializedName("imdb") @Embedded(prefix = "imdb_") val imdb: Rating = Rating(),
                   @SerializedName("kinopoisk") @Embedded(prefix = "kinopoisk_") val kinopoisk: Rating = Rating()) : Serializable {

    @Ignore
    constructor() : this(Rating(), Rating())
}