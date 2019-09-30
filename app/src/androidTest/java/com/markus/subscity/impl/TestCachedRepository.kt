package com.markus.subscity.impl

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

    var testSyncTime: Array<String> = emptyArray()

    init {
        deleteCacheStamp()
    }

    fun getData(): String {
        val isCacheActual = isCacheActual().blockingGet()
        return if (isCacheActual) {
            FROM_DB
        } else {
            updateCacheTimestamp()
            FROM_SERVER
        }
    }

    override fun getDefaultCacheKey() = "test_key"

    override fun getSyncTime() = testSyncTime

}