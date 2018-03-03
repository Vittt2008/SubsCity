package com.source.subscity

import android.app.Application
import com.source.subscity.dagger.DaggerSubsCityComponent
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.dagger.SubsCityModule
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric



/**
 * @author Vitaliy Markus
 */
class SubsCityApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        val subsCityComponent = DaggerSubsCityComponent.builder()
                .subsCityModule(SubsCityModule(this))
                .build()
        SubsCityDagger.init(subsCityComponent)
    }
}