package com.source.subscity

import android.app.Application
import com.source.subscity.dagger.DaggerSubsCityComponent
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.dagger.SubsCityModule

/**
 * @author Vitaliy Markus
 */
class SubsCityApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val subsCityComponent = DaggerSubsCityComponent.builder()
                .subsCityModule(SubsCityModule())
                .build()
        SubsCityDagger.init(subsCityComponent)
    }
}