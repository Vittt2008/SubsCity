package com.markus.subscity

import com.markus.subscity.providers.DatabaseProvider
import com.markus.subscity.repositories.CachedRepository
import com.markus.subscity.providers.DateTimeProvider

/**
 * @author Vitaliy Markus
 */
class TestCachedRepository(databaseProvider: DatabaseProvider,
                           dateTimeProvider: DateTimeProvider) : CachedRepository(databaseProvider, dateTimeProvider) {

    companion object {
        const val FROM_SERVER = "from_server"
        const val FROM_DB = "from_db"
    }

    var testSyncTime: Array<String> = arrayOf("05:55", "10:05", "14:05", "19:00", "23:00")

    init {
        deleteCacheStamp()
    }

    fun getData(): String {
        val isCacheActual = isCacheActual().blockingGet()
        if (isCacheActual) {
            return FROM_DB
        } else {
            updateCacheTimestamp()
            return FROM_SERVER
        }
    }

    override fun getDefaultCacheKey() = "test_key"

    override fun getSyncTime() = testSyncTime

}