package com.source.subscity.api.entities.movie

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Ratings(@SerializedName("imdb") val imdb: Rating = Rating(),
                   @SerializedName("kinopoisk") val kinopoisk: Rating = Rating()) : Serializable