package com.source.subscity

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.source.subscity.dagger.DaggerSubsCityComponent
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.dagger.SubsCityModule
import io.fabric.sdk.android.Fabric


/**
 * @author Vitaliy Markus
 */
class SubsCityApplication : MultiDexApplication() {

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