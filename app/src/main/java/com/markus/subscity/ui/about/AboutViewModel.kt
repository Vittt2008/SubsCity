package com.markus.subscity.ui.about

import androidx.lifecycle.LiveData
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.viewmodels.BaseViewModel
import com.markus.subscity.viewmodels.LiveEvent
import javax.inject.Inject

class AboutViewModel @Inject constructor(private val cityProvider: CityProvider) : BaseViewModel() {

    private val socialNetworkCommandLiveDataInner = LiveEvent<SocialNetworkCommand>()

    val socialNetworkCommandLiveData: LiveData<SocialNetworkCommand>
        get() = socialNetworkCommandLiveDataInner

    fun openTelegram() {
        socialNetworkCommandLiveDataInner.postValue(SocialNetworkCommand(SocialNetwork.TELEGRAM, cityProvider.cityName, cityProvider.city.socialNetworks.telegram))
    }

    fun openVkontakte() {
        socialNetworkCommandLiveDataInner.postValue(SocialNetworkCommand(SocialNetwork.VKONTAKTE, cityProvider.cityName, cityProvider.city.socialNetworks.vkontakte))
    }

    fun openFacebook() {
        socialNetworkCommandLiveDataInner.postValue(SocialNetworkCommand(SocialNetwork.FACEBOOK, cityProvider.cityName, cityProvider.city.socialNetworks.facebook))
    }

    data class SocialNetworkCommand(
            val socialNetwork: SocialNetwork,
            val city: String,
            val url: String
    )

}