package com.source.subscity.api.entities.cinema

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Location(@SerializedName("address") val address: String = "",
                    @SerializedName("metro") val metro: List<String> = emptyList(),
                    @SerializedName("latitude") val latitude: Double = 0.0,
                    @SerializedName("longitude") val longitude: Double = 0.0) : Serializable