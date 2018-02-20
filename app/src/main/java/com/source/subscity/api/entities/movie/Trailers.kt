package com.source.subscity.api.entities.movie

import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Trailers(@SerializedName("original") val original: String,
                    @SerializedName("russian") val russian: String) : Serializable {

    @Ignore
    constructor() : this("", "")
}