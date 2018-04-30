package com.markus.subscity.repositories

import com.markus.subscity.api.ApiClient
import com.markus.subscity.db.entity.CacheTimestamp
import com.markus.subscity.providers.DatabaseProvider
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

/**
 * @author Vitaliy Markus
 */
abstract class CachedRepository(protected val apiClient: ApiClient, protected val databaseProvider: DatabaseProvider) {

    private val periodParser = PeriodFormatterBuilder().appendHours().appendLiteral(":").appendMinutes().toFormatter()
    private val syncTimes by lazy { calculateSyncTime() }

    protected fun updateCacheTimestamp() {
        updateCacheTimestamp(checkAndGetDefaultKey())
    }

    protected fun updateCacheTimestamp(key: String) {
        val timestamp = CacheTimestamp(key, DateTime.now().millis)
        databaseProvider.currentDatabaseClient.cacheTimestampDao.updateCacheTimestamp(timestamp)
    }

    protected fun isCacheActual(): Single<Boolean> {
        return isCacheActual(checkAndGetDefaultKey(), syncTimes)
    }

    protected fun isCacheActual(key: String): Single<Boolean> {
        return isCacheActual(key, syncTimes)
    }

    protected fun deleteCacheStamp() {
        return deleteCacheStamp(checkAndGetDefaultKey())
    }

    protected fun deleteCacheStamp(key: String) {
        return databaseProvider.currentDatabaseClient.cacheTimestampDao.deleteCacheTimestamp(key)
    }

    protected open fun getDefaultCacheKey(): String? = null

    protected open fun getSyncTime() = emptyArray<String>()

    private fun isCacheActual(key: String, syncTimes: List<DateTime>): Single<Boolean> {
        val now = DateTime.now()
        val syncTime = syncTimes.last { it < now }
        return databaseProvider.currentDatabaseClient.cacheTimestampDao.getCacheTimestamp(key)
                .map { it.timestamp > syncTime.millis }
                .onErrorReturnItem(false)
    }

    private fun checkAndGetDefaultKey(): String {
        val key = getDefaultCacheKey()
        return key
                ?: throw IllegalStateException("To use this method without key, getDefaultCacheKey must return not empty value")
    }

    private fun calculateSyncTime(): List<DateTime> {
        val now = DateTime.now().withTimeAtStartOfDay()
        var times = getSyncTime().map { now.plus(Period.parse(it, periodParser)) }
        times = mutableListOf(times.last().minusDays(1)).apply { addAll(times) }
        return times
    }
}