package com.source.subscity.dagger

import dagger.Component
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Component(modules = [SubsCityModule::class])
@Singleton
interface SubsCityComponent {

}