package com.markus.subscity.dagger

/**
 * @author Vitaliy Markus
 */
object SubsCityDagger {

    @JvmStatic
    lateinit var component: SubsCityComponent
        private set

    @JvmStatic
    fun init(subsCityComponent: SubsCityComponent) {
        component = subsCityComponent
    }
}