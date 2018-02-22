package com.source.subscity.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@Entity
data class CacheTimestamp(@PrimaryKey val key: String,
                          val timestamp: Long) : Serializable