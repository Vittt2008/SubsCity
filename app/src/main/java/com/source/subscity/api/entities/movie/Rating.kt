package com.source.subscity.api.entities.movie

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Rating(@SerializedName("id") val id: Long = 0,
                  @SerializedName("rating") val rating: Double = 0.0,
                  @SerializedName("votes") val votes: Int = 0) : Serializable