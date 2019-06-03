package com.markus.subscity.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.markus.subscity.db.entity.CacheTimestamp
import io.reactivex.Single

/**
 * @author Vitaliy Markus
 */
@Dao
interface CacheTimestampDao {

    @Query("SELECT * FROM CacheTimestamp WHERE key = :key LIMIT 1")
    fun getCacheTimestamp(key: String): Single<CacheTimestamp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateCacheTimestamp(timestamp: CacheTimestamp)

    @Query("DELETE FROM CacheTimestamp WHERE key = :key")
    fun deleteCacheTimestamp(key: String)

}