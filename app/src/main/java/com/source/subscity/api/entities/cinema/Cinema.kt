package com.source.subscity.api.entities.cinema

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import com.source.subscity.db.converters.Converter
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@Entity
@TypeConverters(Converter::class)
data class Cinema(@SerializedName("id") @PrimaryKey val id: Long,
                  @SerializedName("location") @Embedded(prefix = "location_") val location: Location,
                  @SerializedName("movies") val movies: List<Long>,
                  @SerializedName("movies_count") val moviesCount: Int,
                  @SerializedName("name") val name: String,
                  @SerializedName("phones") val phones: List<String>,
                  @SerializedName("urls") val urls: List<String>) : Serializable {

    @Ignore
    constructor() : this(0, Location(), emptyList(), 0, "", emptyList(), emptyList())
}