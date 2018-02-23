package com.source.subscity.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.source.subscity.db.entity.CacheTimestamp
import io.reactivex.Single

/**
 * @author Vitaliy Markus
 */
@Dao
interface CacheTimestampDao {

    @Query("SELECT * FROM CacheTimestamp WHERE key = :arg0 LIMIT 1")
    fun getCacheTimestamp(key: String): Single<CacheTimestamp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateCacheTimestamp(timestamp: CacheTimestamp)

    @Query("DELETE FROM CacheTimestamp WHERE key = :arg0")
    fun deleteCacheTimestamp(key: String)

}