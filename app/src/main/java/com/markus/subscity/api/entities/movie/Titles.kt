package com.markus.subscity.api.entities.movie

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Titles(@SerializedName("original") val original: String,
                  @SerializedName("russian") val russian: String) : Serializable {

    @Ignore
    constructor() : this("", "")
}