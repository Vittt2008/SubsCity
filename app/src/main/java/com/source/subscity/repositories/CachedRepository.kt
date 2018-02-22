package com.source.subscity.repositories

import com.source.subscity.api.ApiClient
import com.source.subscity.db.DatabaseClient
import com.source.subscity.db.entity.CacheTimestamp
import io.reactivex.Single
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
abstract class CachedRepository(protected val apiClient: ApiClient, protected val databaseClient: DatabaseClient) {

    protected fun updateCacheTimestamp() {
        updateCacheTimestamp(checkAndGetDefaultKey())
    }

    protected fun updateCacheTimestamp(key: String) {
        val timestamp = CacheTimestamp(key, DateTime.now().millis)
        databaseClient.cacheTimestampDao.updateCacheTimestamp(timestamp)
    }

    protected fun isCacheActual(): Single<Boolean> {
        return isCacheActual(checkAndGetDefaultKey(), getCacheLifetime())
    }

    protected fun isCacheActual(key: String): Single<Boolean> {
        return isCacheActual(key, getCacheLifetime(key))
    }

    protected fun deleteCacheStamp() {
        return deleteCacheStamp(checkAndGetDefaultKey())
    }

    protected fun deleteCacheStamp(key: String) {
        return databaseClient.cacheTimestampDao.deleteCacheTimestamp(key)
    }

    protected open fun getDefaultCacheKey(): String? = null

    protected open fun getCacheLifetime() = 0L

    protected open fun getCacheLifetime(key: String) = 0L

    private fun isCacheActual(key: String, lifetime: Long): Single<Boolean> {
        return Single.fromCallable {
            val current = DateTime.now().millis
            val start = current - lifetime
            val cacheTimestamp = databaseClient.cacheTimestampDao.getCacheTimestamp(key)
            val isActual: Boolean = cacheTimestamp?.let { it.timestamp in start..current } ?: false
            return@fromCallable isActual
        }
    }

    private fun checkAndGetDefaultKey(): String {
        val key = getDefaultCacheKey()
        if (key.isNullOrEmpty()) {
            throw IllegalStateException("To use this method without key, getDefaultCacheKey must return not empty value")
        }
        return key!!
    }
}