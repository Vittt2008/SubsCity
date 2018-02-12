package com.source.subscity.api.entities.cinema

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
data class Cinema(@SerializedName("id") val id: Long = 0,
                  @SerializedName("location") val location: Location = Location(),
                  @SerializedName("movies") val movies: List<Long> = emptyList(),
                  @SerializedName("movies_count") val moviesCount: Int = 0,
                  @SerializedName("name") val name: String = "",
                  @SerializedName("phones") val phones: List<String> = emptyList(),
                  @SerializedName("urls") val urls: List<String> = emptyList()) : Serializable