package com.source.subscity.api.entities.cinema

import android.arch.persistence.room.Ignore
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.source.subscity.db.converters.LongListConverter
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@TypeConverters(LongListConverter::class)
data class Location(@SerializedName("address") val address: String,
                    @SerializedName("metro") val metro: List<String>,
                    @SerializedName("latitude") val latitude: Double,
                    @SerializedName("longitude") val longitude: Double) : Serializable {

    @Ignore
    constructor() : this("", emptyList(), 0.0, 0.0)
}