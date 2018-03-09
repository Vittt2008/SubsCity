package com.source.subscity.repositories

import com.source.subscity.api.ApiClient
import com.source.subscity.db.entity.CacheTimestamp
import com.source.subscity.providers.DatabaseProvider
import io.reactivex.Single
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
abstract class CachedRepository(protected val apiClient: ApiClient, protected val databaseProvider: DatabaseProvider) {

    protected fun updateCacheTimestamp() {
        updateCacheTimestamp(checkAndGetDefaultKey())
    }

    protected fun updateCacheTimestamp(key: String) {
        val timestamp = CacheTimestamp(key, DateTime.now().millis)
        databaseProvider.currentDatabaseClient.cacheTimestampDao.updateCacheTimestamp(timestamp)
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
        return databaseProvider.currentDatabaseClient.cacheTimestampDao.deleteCacheTimestamp(key)
    }

    protected open fun getDefaultCacheKey(): String? = null

    protected open fun getCacheLifetime() = 0L

    protected open fun getCacheLifetime(key: String) = getCacheLifetime()

    private fun isCacheActual(key: String, lifetime: Long): Single<Boolean> {
        val current = DateTime.now().millis
        val start = current - lifetime
        return databaseProvider.currentDatabaseClient.cacheTimestampDao.getCacheTimestamp(key)
                .map { it.timestamp in start..current }
                .onErrorReturnItem(false)
    }

    private fun checkAndGetDefaultKey(): String {
        val key = getDefaultCacheKey()
        if (key.isNullOrEmpty()) {
            throw IllegalStateException("To use this method without key, getDefaultCacheKey must return not empty value")
        }
        return key!!
    }
}