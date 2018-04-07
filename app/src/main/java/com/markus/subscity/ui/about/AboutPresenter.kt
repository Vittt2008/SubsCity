package com.markus.subscity.ui.about

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.CityProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class AboutPresenter @Inject constructor(private val cityProvider: CityProvider) : MvpPresenter<AboutView>() {

    fun openTelegram() {
        viewState.openSocialNetwork(cityProvider.city.socialNetworks.telegram)
    }

    fun openVkontakte() {
        viewState.openSocialNetwork(cityProvider.city.socialNetworks.vkontakte)
    }

    fun openFacebook() {
        viewState.openSocialNetwork(cityProvider.city.socialNetworks.facebook)
    }
}