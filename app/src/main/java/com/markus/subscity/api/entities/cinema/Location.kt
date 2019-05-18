package com.markus.subscity.api.entities.cinema

import androidx.room.Ignore
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.markus.subscity.db.converters.Converter
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@TypeConverters(Converter::class)
data class Location(@SerializedName("address") val address: String,
                    @SerializedName("metro") val metro: List<String>,
                    @SerializedName("latitude") val latitude: Double,
                    @SerializedName("longitude") val longitude: Double) : Serializable {

    @Ignore
    constructor() : this("", emptyList(), 0.0, 0.0)
}