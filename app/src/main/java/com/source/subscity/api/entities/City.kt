package com.source.subscity.api.entities

/**
 * @author Vitaliy Markus
 */
data class City(val id: String,
                val name: String,
                val location: Location,
                val socialNetworks: SocialNetworks)

data class Location(val latitude: Double,
                    val longitude: Double,
                    val zoom: Int)

data class SocialNetworks(val telegram: String,
                          val vkontakte: String,
                          val facebook: String)