package com.markus.subscity

import android.content.Context
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.PreferencesProvider
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * @author Vitaliy Markus
 */
class CityProviderTest {
    private val context: Context = mock(defaultAnswer = Mockito.RETURNS_DEEP_STUBS)
    private val preferencesProvider: PreferencesProvider = mock(defaultAnswer = Mockito.RETURNS_DEEP_STUBS)
    private lateinit var cityProvider: CityProvider
    private val defaultCityId = CityProvider.SAINT_PETERSBURG

    @Before
    fun setUp() {
        whenever(preferencesProvider.getAppPreferences().getString(eq(PreferencesProvider.CITY_ID_KEY), any())).thenReturn(defaultCityId)
        whenever(context.getString(R.string.saint_petersburg)).thenReturn(CityProvider.SAINT_PETERSBURG)
        whenever(context.getString(R.string.moscow)).thenReturn(CityProvider.MOSCOW)
        cityProvider = CityProvider(context, preferencesProvider)
    }

    @Test
    fun `check default cityId`() {
        val cityId = cityProvider.cityId
        assertTrue(cityId == defaultCityId)
    }

    @Test
    fun `check change cityId`() {
        val testObserver = cityProvider.asyncCity.test()
        val newCityId = CityProvider.MOSCOW
        cityProvider.changeCity(newCityId)

        verify(preferencesProvider.getAppPreferences().edit()).putString(PreferencesProvider.CITY_ID_KEY, newCityId)
        assertTrue(cityProvider.cityId == newCityId)
        assertTrue(testObserver.values().size == 2)
        assertTrue(testObserver.values().first() == defaultCityId)
        assertTrue(testObserver.values()[1] == newCityId)
    }
}