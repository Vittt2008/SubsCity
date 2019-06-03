package com.markus.subscity.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author Vitaliy Markus
 */
@Entity
data class CacheTimestamp(@PrimaryKey val key: String,
                          val timestamp: Long) : Serializable