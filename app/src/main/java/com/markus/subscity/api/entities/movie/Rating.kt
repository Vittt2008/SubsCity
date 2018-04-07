package com.markus.subscity.api.entities.movie

import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Rating(@SerializedName("id") val id: Long,
                  @SerializedName("rating") val rating: Double,
                  @SerializedName("votes") val votes: Int) : Serializable {

    @Ignore
    constructor() : this(0, 0.0, 0)
}