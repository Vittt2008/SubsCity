package com.markus.subscity.ui.about

import com.arellomobile.mvp.InjectViewState
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.ui.base.BaseMvpPresenter
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class AboutPresenter @Inject constructor(private val cityProvider: CityProvider) : BaseMvpPresenter<AboutView>() {

    fun openTelegram() {
        viewState.openSocialNetwork(AboutView.SocialNetwork.TELEGRAM, cityProvider.cityName, cityProvider.city.socialNetworks.telegram)
    }

    fun openVkontakte() {
        viewState.openSocialNetwork(AboutView.SocialNetwork.VKONTAKTE, cityProvider.cityName, cityProvider.city.socialNetworks.vkontakte)
    }

    fun openFacebook() {
        viewState.openSocialNetwork(AboutView.SocialNetwork.FACEBOOK, cityProvider.cityName, cityProvider.city.socialNetworks.facebook)
    }
}